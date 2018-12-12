package com.stylefeng.guns.modular.bdp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.*;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.model.JobSet;
import com.stylefeng.guns.modular.system.model.JobStat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 * 任务信息表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
@Service
public class JobRestServiceImpl implements IJobRestService {

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


	/***
	 * 任务开始执行前调用此方法
	 * @param jobName
	 * @param number
	 * @param stat_date
	 * @param params
	 */
	public void start(String jobName, String number, String stat_date, String params) {
		//获取任务信息并赋值任务集名称
		JobInfo jobInfo= resolveJobInfo(jobName);
		//任务存在且最新运行状态不是运行中
		if (jobInfo != null && (jobInfo.getLastRunState()==null||!jobInfo.getLastRunState().equals(LastRunState.RUNNING.getCode()))) {
			//任务最新状态
			jobInfo.setLastRunState(LastRunState.RUNNING.getCode());
			jobInfo.setLastRunTime(DateTimeKit.parseDateTime(DateTimeKit.now()));
			jobInfo.setLastRunCost(0l);
			jobInfo.setLastRunNum(Integer.valueOf(number));
			jobInfoService.updateById(jobInfo);
			// job_stat 增加一个正在运行
			JobStat jobStat = jobStatService.selectOne(new EntityWrapper<JobStat>().eq("stat_date", stat_date));
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
			jobRunHistory.setNum(Long.valueOf(number));
			jobRunHistory.setTime(DateTimeKit.parseDateTime(DateTimeKit.now()));
			jobRunHistoryService.insert(jobRunHistory);
		}
	}

	/***
	 * 任务结束后调用此方法
	 * @param jobNamePath
	 * @param number
	 * @param stat_date
	 */
	public void end(String jobNamePath, String number, String stat_date) {
		//获取任务信息并赋值任务集名称
		JobInfo jobInfo= resolveJobInfo(jobNamePath);
		if (jobInfo != null) {
			//如果此任务是运行中的任务则将统计结果修改 否则仅仅同步运行状态
			if(jobInfo.getLastRunState()!=null&&jobInfo.getLastRunState().equals(LastRunState.RUNNING.getCode())) {
				// 同步jenkins运行的信息 每1秒查看jenkins是否完成
                while (true){
                    try {
                    Thread.sleep(1000);
                    BuildWithDetails buildWithDetails = new JobUtil(jobInfo.getJobSetName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken())
                                .getJob(jobInfo.getName())
                                .getBuildByNumber(Integer.valueOf(number))
                                .details();
                        if (!buildWithDetails.isBuilding()){
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
				String buildResult=sync(jobNamePath,number);
				if(buildResult!=null) {
					// job_stat 去掉一个正在运行的加上一个运行成功或者失败的
					JobStat jobStat = jobStatService.selectOne(new EntityWrapper<JobStat>().eq("stat_date", stat_date));
					if (jobStat != null) {
						if (jobStat.getRunning() > 0) {
							jobStat.setRunning(jobStat.getRunning() - 1);
							if (buildResult.equals("success")) {
								jobStat.setSuccess(jobStat.getSuccess() + 1);
							} else {
								jobStat.setFail(jobStat.getFail() + 1);
							}
							jobStatService.updateById(jobStat);
						}
					}
				}
			}
		}
	}

	/***
	 * 同步jenkins信息到数据库
	 * @param jobNamePath
	 * @param number
	 * @return
	 */
	public String sync(String jobNamePath, String number) {

		String buildResult = null;
		Long duration = 0l;
		String consoleOutputText = "";
		//获取任务信息并赋值任务集名称
		JobInfo jobInfo= resolveJobInfo(jobNamePath);
		if (jobInfo != null) {
			try {
				BuildWithDetails buildWithDetails = new JobUtil(jobInfo.getJobSetName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken())
						.getJob(jobInfo.getName())
						.getBuildByNumber(Integer.valueOf(number))
						.details();
				buildResult = buildWithDetails.getResult().name().toLowerCase();
				duration = buildWithDetails.getDuration();
				consoleOutputText = buildWithDetails.getConsoleOutputText();
			} catch (Exception ex) {
				buildResult="fail";
				consoleOutputText = ex.getMessage();
			} finally {
				//job_run_history 根据jobName和构建num更新   statLastRunState   cost log
				JobRunHistory jobRunHistory = jobRunHistoryService.selectOne(new EntityWrapper<JobRunHistory>().eq("job_info_id", jobInfo.getId()).eq("num", number));
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
                //更新任务最新状态
				if (buildResult.equals("success")) {
					jobInfo.setLastRunState(LastRunState.SUCCESS.getCode());
				} else {
					jobInfo.setLastRunState(LastRunState.FAIL.getCode());
				}
				jobInfo.setLastRunCost(duration);
				jobInfoService.updateById(jobInfo);
				return buildResult;
			}
		}
		return null;
	}


	/***
	 * 根据传入的任务全路径获取任务的信息
	 * @param jobNamePath zenith01/job01 这种类型的全路径
	 * @return
	 */
	private JobInfo resolveJobInfo(String jobNamePath){
		JobInfo jobInfo=null;
		if (StringUtils.isNotEmpty(jobNamePath)) {
			String[] re = jobNamePath.split("/");
			if (re.length == 2) {
				String procName = re[0];
				String jobName = re[1];
				JobSet jobSet = jobSetService.selectOne(new EntityWrapper<JobSet>().eq("name", procName));
				if (jobSet != null) {
					jobInfo =jobInfoService.selectOne(new EntityWrapper<JobInfo>().eq("name", jobName).eq("job_set_id", jobSet.getId()));
					if (jobInfo!=null){
						//设置任务集的名称
						jobInfo.setJobSetName(procName);
					}
				}
			}
		}
		return jobInfo;
	}
}
