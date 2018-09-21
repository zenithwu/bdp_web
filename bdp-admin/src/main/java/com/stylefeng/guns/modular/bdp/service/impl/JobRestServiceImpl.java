package com.stylefeng.guns.modular.bdp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;
import com.stylefeng.guns.modular.bdp.service.IJobStatService;
import com.stylefeng.guns.modular.system.dao.JobInfoConfMapper;
import com.stylefeng.guns.modular.system.dao.JobInfoMapper;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.model.JobSet;
import com.stylefeng.guns.modular.system.model.JobStat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务信息表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
@Service
public class JobRestServiceImpl {

	@Autowired
	private JenkinsConfig jenkinsConfig;
	@Autowired
	private IJobInfoService jobInfoService;
	@Autowired
	private IJobSetService jobSetService;
	@Autowired
	private IJobStatService jobStatService;
	@Autowired
	private IJobRunHistoryService jobRunHistoryService;

	public void start(String jobName, String number, String stat_date, String params) {
		if (StringUtils.isNotEmpty(jobName)) {
			String[] re = jobName.split("/");
			if (re.length == 2) {
				String procName = re[0];
				jobName = re[1];
				String buildNum = number;

				Wrapper<JobSet> jsWrapper = new EntityWrapper<>();
				JobSet jobSet = jobSetService.selectOne(jsWrapper.eq("name", procName));
				if (jobSet != null) {
					Wrapper<JobInfo> jiWrapper = new EntityWrapper<>();
					JobInfo jobInfo = jobInfoService.selectOne(jiWrapper.eq("name", jobName).eq("job_set_id", jobSet.getId()));
					if (jobInfo != null) {
						//任务最新状态
						jobInfo.setLastRunState(LastRunState.RUNNING.getCode());
						jobInfo.setLastRunTime(DateTimeKit.parseDateTime(DateTimeKit.now()));
						jobInfo.setLastRunCost(0l);
						jobInfoService.updateById(jobInfo);
						// job_stat 增加一个正在运行
						Wrapper<JobStat> statWrapper = new EntityWrapper<>();
						JobStat jobStat = jobStatService.selectOne(statWrapper.eq("stat_date", stat_date));
						if (jobStat == null) {
							jobStat = new JobStat();
							jobStat.setRunning(1);
							jobStat.setFail(0);
							jobStat.setSuccess(0);
							jobStat.setStatDate(DateTimeKit.parseDate(stat_date));
						} else {
							jobStat.setRunning(jobStat.getRunning() + 1);
						}
						jobStatService.insertOrUpdate(jobStat);
						//job_run_history 插入信息
						JobRunHistory jobRunHistory = new JobRunHistory();
						jobRunHistory.setJobInfoId(jobInfo.getId());
						jobRunHistory.setParams(params);
						jobRunHistory.setState(LastRunState.RUNNING.getCode());
						jobRunHistory.setNum(Long.valueOf(buildNum));
						jobRunHistory.setTime(DateTimeKit.parseDateTime(DateTimeKit.now()));
						jobRunHistoryService.insert(jobRunHistory);
					}

				}
			}
		}
	}

	public void end(String jobName, String number, String stat_date) {
		if (StringUtils.isNotEmpty(jobName)) {
			String[] re = jobName.split("/");
			if (re.length == 2) {
				String procName = re[0];
				jobName = re[1];
				String buildNum = number;
				Wrapper<JobSet> jsWrapper = new EntityWrapper<>();
				JobSet jobSet = jobSetService.selectOne(jsWrapper.eq("name", procName));
				if (jobSet != null) {

					Wrapper<JobInfo> jiWrapper = new EntityWrapper<>();
					JobInfo jobInfo = jobInfoService.selectOne(jiWrapper.eq("name", jobName).eq("job_set_id", jobSet.getId()));
					if (jobInfo != null) {
						//等待执行完毕
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {

						}
						String buildResult = "fail";
						Long duration = 0l;
						String consoleOutputText = "";

						try {
							BuildWithDetails buildWithDetails = new JobUtil(procName, jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken())
									.getJob(jobName)
									.getBuildByNumber(Integer.valueOf(buildNum))
									.details();
							buildResult = buildWithDetails.getResult().name().toLowerCase();
							duration = buildWithDetails.getDuration();
							consoleOutputText = buildWithDetails.getConsoleOutputText();
						} catch (Exception ex) {
							consoleOutputText = ex.getMessage();
						} finally {
							//任务最新状态
							if (buildResult.equals("success")) {
								jobInfo.setLastRunState(LastRunState.SUCCESS.getCode());
							} else {
								jobInfo.setLastRunState(LastRunState.FAIL.getCode());
							}
							jobInfo.setLastRunCost(duration);
							jobInfoService.updateById(jobInfo);

							// job_stat 去掉一个正在运行的加上一个运行成功或者失败的
							JobStat jobStat = jobStatService.selectOne(new EntityWrapper<JobStat>().eq("stat_date", stat_date));
							if (jobStat != null) {
								if(jobStat.getRunning()>0){
									jobStat.setRunning(jobStat.getRunning()- 1);
									if (buildResult.equals("success")) {
										jobStat.setSuccess(jobStat.getSuccess() + 1);
									} else {
										jobStat.setFail(jobStat.getFail() + 1);
									}
									jobStatService.updateById(jobStat);
								}
							}
							//job_run_history 根据jobName和构建num更新   statLastRunState   cost log
							JobRunHistory jobRunHistory = jobRunHistoryService.selectOne(new EntityWrapper<JobRunHistory>().eq("job_info_id", jobInfo.getId()).eq("num", buildNum));
							if (jobRunHistory != null) {
								if (buildResult.equals("success")) {
									jobRunHistory.setState(LastRunState.SUCCESS.getCode());
								} else {
									jobRunHistory.setState(LastRunState.FAIL.getCode());
								}
								jobRunHistory.setCost(duration);
								jobRunHistory.setLog(consoleOutputText);
								jobRunHistoryService.updateById(jobRunHistory);
							}

						}
					}
				}

			}
		}
	}

}
