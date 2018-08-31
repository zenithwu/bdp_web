package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;
import com.stylefeng.guns.modular.bdp.service.IJobStatService;
import com.stylefeng.guns.modular.bdp.service.impl.JobInfoServiceImpl;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.model.JobSet;
import com.stylefeng.guns.modular.system.model.JobStat;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 任务信息控制器
 *
 * @author fengshuonan
 * @Date 2018-08-23 18:36:33
 */
@Controller
@RequestMapping("/rest")
public class JobRestController extends BaseController {

    @Autowired
    private  JenkinsConfig jenkinsConfig;
    @Autowired
    private IJobInfoService jobInfoService;
    @Autowired
    private IJobSetService jobSetService;
    @Autowired
    private IJobStatService jobStatService;
    @Autowired
    private IJobRunHistoryService jobRunHistoryService;

    @RequestMapping(value = "/job_begin", method = RequestMethod.POST)
    @ResponseBody
    public Object job_begin() {
        //jobName zenith/test1     或者 test1
        //number  10 构建编号
        //stat_date 2018-01-01  任务开始执行的时间一般为当前时间
        //params 2018-01-01-01-11
        String jobName = super.getPara("jobName");
        String number=super.getPara("number");
        String stat_date=super.getPara("stat_date");
        String params=super.getPara("params");
        if (StringUtils.isNotEmpty(jobName)){
            String[] re=jobName.split("/");
            if (re.length==2){
                String procName=re[0];
                jobName=re[1];
                String buildNum=number;

                    Wrapper<JobSet> jsWrapper = new EntityWrapper<>();
                    JobSet jobSet = jobSetService.selectOne(jsWrapper.eq("name", procName));
                    if (jobSet != null) {
                        Wrapper<JobInfo> jiWrapper = new EntityWrapper<>();
                        JobInfo jobInfo = jobInfoService.selectOne(jiWrapper.eq("name", jobName).eq("job_set_id",jobSet.getId()));
                        if (jobInfo != null) {
                            //任务最新状态
                            jobInfo.setLastRunState(LastRunState.RUNNING.getCode());
                            jobInfo.setLastRunTime(DateTimeKit.parseDate(stat_date));
                            jobInfoService.updateById(jobInfo);
                            // job_stat 增加一个正在运行
                            Wrapper<JobStat> statWrapper = new EntityWrapper<>();
                            JobStat jobStat = jobStatService.selectOne(statWrapper.eq("stat_date", stat_date));
                            if (jobStat == null) {
                                jobStat = new JobStat();
                                jobStat.setRunning(1);
                                jobStat.setCount(0);
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
                            jobRunHistory.setTime(DateTimeKit.parseDate(stat_date));
                            jobRunHistoryService.insert(jobRunHistory);
                        }

                    }
            }
        }
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/job_end", method = RequestMethod.POST)
    @ResponseBody
    public Object job_end() {
        //jobName zenith/test1     或者 test1
        //number  10 构建编号
        String jobName = super.getPara("jobName");
        String number=super.getPara("number");
        String stat_date=super.getPara("stat_date");
        if (StringUtils.isNotEmpty(jobName)){
            String[] re=jobName.split("/");
            if (re.length==2){
                String procName=re[0];
                jobName=re[1];
                String buildNum=number;
                try {
                    Wrapper<JobSet> jsWrapper = new EntityWrapper<>();
                    JobSet jobSet = jobSetService.selectOne(jsWrapper.eq("name", procName));
                    if(jobSet!=null){

                        Wrapper<JobInfo> jiWrapper = new EntityWrapper<>();
                        JobInfo jobInfo = jobInfoService.selectOne(jiWrapper.eq("name", jobName).eq("job_set_id",jobSet.getId()));
                        if (jobInfo!=null){

                            BuildWithDetails buildWithDetails=new JobUtil(procName,jenkinsConfig.getUrl(),jenkinsConfig.getUser(),jenkinsConfig.getToken())
                                    .getJob(jobName)
                                    .getBuildByNumber(Integer.valueOf(buildNum))
                                    .details();
                            //任务最新状态
                            if (buildWithDetails.getResult().name().toLowerCase().equals("success")) {
                                jobInfo.setLastRunState(LastRunState.SUCCESS.getCode());
                            } else {
                                jobInfo.setLastRunState(LastRunState.FAIL.getCode());
                            }
                            jobInfo.setLastRunCost(buildWithDetails.getDuration());
                            jobInfoService.updateById(jobInfo);

                            // job_stat 去掉一个正在运行的加上一个运行成功或者失败的
                            Wrapper<JobStat> statWrapper = new EntityWrapper<>();
                            JobStat jobStat = jobStatService.selectOne(statWrapper.eq("stat_date", stat_date));
                            if(jobStat!=null) {
                                jobStat.setRunning(jobStat.getRunning() - 1);
                                if (buildWithDetails.getResult().name().toLowerCase().equals("success")) {
                                    jobStat.setSuccess(jobStat.getSuccess() + 1);
                                } else {
                                    jobStat.setFail(jobStat.getFail() + 1);
                                }
                                jobStatService.updateById(jobStat);
                            }
                            //job_run_history 根据jobName和构建num更新   statLastRunState   cost log
                            Wrapper<JobRunHistory> hisWrapper = new EntityWrapper<>();
                            JobRunHistory jobRunHistory = jobRunHistoryService.selectOne(hisWrapper.eq("job_info_id",jobInfo.getId()).eq("num",buildNum));
                            if(jobRunHistory!=null){
                                if (buildWithDetails.getResult().name().toLowerCase().equals("success")) {
                                    jobRunHistory.setState(LastRunState.SUCCESS.getCode());
                                } else {
                                    jobRunHistory.setState(LastRunState.FAIL.getCode());
                                }
                                jobRunHistory.setCost(buildWithDetails.getDuration());
                                jobRunHistory.setLog(buildWithDetails.getConsoleOutputText());
                                jobRunHistoryService.updateById(jobRunHistory);
                            }
                        }
                 }
            } catch (Exception e) {
                        return e.getMessage(); }
            }
        }
        return SUCCESS_TIP;
    }


}
