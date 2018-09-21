package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.stylefeng.guns.config.properties.BdpJobConfig;
import com.stylefeng.guns.config.properties.HiveConfig;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizException;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.constant.JobStatus;
import com.stylefeng.guns.core.constant.JobType;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.constant.conTypeType;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.HdfsUtil;
import com.stylefeng.guns.core.util.HiveUtil;
import com.stylefeng.guns.core.util.JobConfUtil;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.*;
import com.stylefeng.guns.modular.bdp.service.impl.JobRestServiceImpl;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.core.common.exception.BizExceptionEnum.SERVER_ERROR;

/**
 * 任务信息控制器
 *
 * @author fengshuonan
 * @Date 2018-08-23 18:36:33
 */
@Controller
@RequestMapping("/jobInfo")
public class JobInfoController extends BaseController {

    private String PREFIX = "/bdp/jobInfo/";
    private String DETAIL = "/bdp/job_detail/";
    private Logger logger=Logger.getLogger(JobInfoController.class);
    @Autowired
    private IJobInfoService jobInfoService;

    @Autowired
    private IJobRunHistoryService jobRunHistoryService;

    @Autowired
    private IJobSetService jobSetService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IJobInfoConfService jobInfoConfService;
    @Autowired
    private IConfConnectService confConnectService;
    @Autowired
    private IConfConnectTypeService confConnectTypeService;
    @Autowired
    private JenkinsConfig jenkinsConfig;
    @Autowired
    private HiveConfig hiveConfig;
    @Autowired
    private BdpJobConfig bdpJobConfig;
    @Autowired
    private JobRestServiceImpl jobRestService;
    /**
     * 跳转到任务信息首页
     */
    @RequestMapping("")
    public String index() {

        List<User> userList=userService.selectList(new EntityWrapper<User>().eq("status",1));
        super.setAttr("userList", userList);
        super.setAttr("userId", ShiroKit.getUser().getId());
        return PREFIX + "jobInfo.html";
    }

    /**
     * 跳转到添加任务信息
     */
    @RequestMapping("/jobInfo_add")
    public String jobInfoAdd() {
        List<JobSet> jobSets = jobSetService.selectList(null);
        super.setAttr("jobSetList", jobSets);
        return PREFIX + "jobInfo_add.html";
    }

    /**
     * 跳转到修改任务信息
     */
    @RequestMapping("/jobInfo_config/{jobInfoId}")
    public String jobInfo_config(@PathVariable Integer jobInfoId, Model model) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        String pageName = JobType.ObjOf(jobInfo.getTypeId()).getPage();

        //查询任务的所有参数
        List<JobInfoConf> jobInfoConfList = jobInfoConfService.selJobInfoConfByJobInfoId(jobInfoId);
        JobConfig jobConfig = JobConfig.listToJobConfig(jobInfoConfList);
        //展示的时候将依赖的前后的逗号去掉
        if(StringUtils.isNotEmpty(jobConfig.getSchedule_depend())){
            String depends=jobConfig.getSchedule_depend();
            if(depends.startsWith(",")) {
                depends=depends.replaceFirst(",", "");
            }
            if(depends.endsWith(",")){
                depends=depends.substring(0,depends.length()-1);
            }
            jobConfig.setSchedule_depend(depends);
        }


        model.addAttribute("item", jobInfo);
        model.addAttribute("jobConfig", jobConfig);
        ////数据接入 和 数据推送
        if (jobInfo.getTypeId() == JobType.INPUT.getCode() ) {
            showInputData(model, jobConfig);
        }
        if (jobInfo.getTypeId() == JobType.OUTPUT.getCode()) {
            showOutputData(model, jobConfig);
        }
        LogObjectHolder.me().set(jobInfo);
        return DETAIL + pageName;
    }

    private void showInputData(Model model, JobConfig jobConfig) {
        //所有连接类型
        List<ConfConnectType> allConfConnectType = confConnectTypeService.selectAllConfConnectType();

        //查询该连接所属的类型下的所有的连接
        List<ConfConnect> connects = new ArrayList<>();
        if (jobConfig.getInput_connect_id() != null) {
            ConfConnect connect = confConnectService.selectById(jobConfig.getInput_connect_id());
            if (connect != null) {
                //设置连接类型id
                jobConfig.setInput_connect_type(connect.getTypeId());
                //查询该连接所属的类型下的所有的连接
                connects = confConnectService.selectList(new EntityWrapper<ConfConnect>().eq("type_id", connect.getTypeId()));
            }
        }
        model.addAttribute("allConfConnectType", allConfConnectType);
        model.addAttribute("connects", connects);

        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        //查询输出的数据库集合
        List<String> dbList = hiveUtil.getDataBases();
        //查询输出的该数据库的所有的表的集合
        List<String> tableList = new ArrayList<>();
        if (jobConfig.getOutput_db_name() != null) {
            tableList = hiveUtil.getTablesByDbName(jobConfig.getOutput_db_name());
        }
        model.addAttribute("dbList", dbList);
        model.addAttribute("tableList", tableList);
    }
    private void showOutputData(Model model, JobConfig jobConfig) {
        //所有连接类型
        List<ConfConnectType> allConfConnectType = confConnectTypeService.selectAllConfConnectType();

        //查询该连接所属的类型下的所有的连接
        List<ConfConnect> connects = new ArrayList<>();
        if (jobConfig.getOutput_connect_id() != null) {
            ConfConnect connect = confConnectService.selectById(jobConfig.getOutput_connect_id());
            if (connect != null) {
                //设置连接类型id
                jobConfig.setOutput_connect_type(connect.getTypeId());
                //查询该连接所属的类型下的所有的连接
                connects = confConnectService.selectList(new EntityWrapper<ConfConnect>().eq("type_id", connect.getTypeId()));
            }
        }
        model.addAttribute("allConfConnectType", allConfConnectType);
        model.addAttribute("connects", connects);

    }



    /**
     * 获取任务信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition,String userId) {
        userId=StringUtils.isEmpty(userId)?String.valueOf(ShiroKit.getUser().getId()):userId;
        Wrapper<JobInfo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("name", condition).eq("user_info_id",userId).orderBy("create_time",false);
        List<JobInfo> list = jobInfoService.selectList(wrapper);
        for (JobInfo info : list
                ) {
            info.setEnableName(JobStatus.ObjOf(info.getEnable()).getName());
            if(info.getLastRunState()!=null) {
                info.setLastRunStateName(LastRunState.ObjOf(info.getLastRunState()).getName());
            }
            if(null!=info.getCreatePer()) {
                User user=userService.selectById(info.getCreatePer());
                info.setCreatePerName(user!=null?user.getName():"");
            }
            if(null!=info.getModPer()) {
                User user=userService.selectById(info.getModPer());
                info.setModPerName(user!=null?user.getName():"");
            }
            if(null!=info.getUserInfoId()) {
                User user=userService.selectById(info.getUserInfoId());
                info.setUserInfoName(user!=null?user.getName():"");
            }
            if (info.getJobSetId() != null) {
                info.setJobSetName(jobSetService.selectById(info.getJobSetId()).getName());
            }
            info.setTypeName(JobType.ObjOf(info.getTypeId()).getName());
        }
        return list;
    }

    /**
     * 新增任务信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobInfo jobInfo) {
        JobInfo job = jobInfoService.selJobInfoByName(jobInfo.getName());
        if (job != null) {
            throw new GunsException(BizExceptionEnum.JOBINFO_EXISTED);
        } else {
            jobInfo.setCreatePer(ShiroKit.getUser().getId());
            jobInfo.setCreateTime(DateTimeKit.date());
            jobInfo.setUserInfoId(ShiroKit.getUser().getId());
            //启用状态
            jobInfo.setEnable(0);
            if (jobInfoService.insert(jobInfo)) {
                JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
                try {
                    jobUtil.createJob(jobInfo.getName(), jobInfo.getDesc(), JobConfUtil.wrapShell("",jenkinsConfig));
                    return SUCCESS_TIP;
                } catch (IOException e) {
                    throw new GunsException(SERVER_ERROR);
                }

            }
            return SUCCESS_TIP;
        }
    }




    /**
     * 删除任务信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobInfoId) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        if ((jobInfo.getLastRunState() != null) && LastRunState.RUNNING.getCode() == jobInfo.getLastRunState()) {
            throw new GunsException(BizExceptionEnum.JOBINFO_RUN);
        }
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        List<JobInfo> icList = jobInfoService.selJobDependByJobId(jobInfoId);
        if (icList.size() > 0 && icList != null) {
            throw new GunsException(BizExceptionEnum.JOBINFOCOF_JOBINFO);
        } else {
            if (jobInfoService.deleteById(jobInfoId)) {
                if (jobInfoConfService.delete(new EntityWrapper<JobInfoConf>().eq("job_info_id", jobInfoId))) {

                    JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
                    try {
                        jobUtil.deleteJob(jobInfo.getName());
                        return SUCCESS_TIP;
                    } catch (IOException e) {
                        throw new GunsException(SERVER_ERROR);
                    }
                }
            }
            return SUCCESS_TIP;
        }
    }


    @RequestMapping(value = "/stop")
    @ResponseBody
    public Object stop(@RequestParam Integer jobInfoId) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        if ((jobInfo.getLastRunState() == null) || LastRunState.RUNNING.getCode() != jobInfo.getLastRunState()) {
            throw new GunsException(BizExceptionEnum.JOBINFO_NOTRUNNING);
        }
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
        try {
            jobUtil.stopJob(jobInfo.getName());
            JobWithDetails jobWithDetails=jobUtil.getJob(jobInfo.getName());
            Integer lastBuildNum=jobWithDetails.getLastBuild().getNumber();
            jobRestService.end(jobWithDetails.getFullName(),String.valueOf(lastBuildNum),DateTimeKit.formatDate(jobInfo.getLastRunTime()));
            return SUCCESS_TIP;
        } catch (IOException e) {
            throw new GunsException(SERVER_ERROR);
        }

    }

    /**
     * 启用任务
     *
     * @return
     */
    @RequestMapping(value = "/enableJobInfo")
    @ResponseBody
    public Object enableJobInfo(@RequestParam Integer jobInfoId) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        if (jobInfoService.enableJobInfo(jobInfoId) > 0) {
            JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
            try {
                jobUtil.enableJob(jobInfo.getName());
                return SUCCESS_TIP;
            } catch (Exception e) {
                throw new GunsException(SERVER_ERROR);
            }
        }
        return SUCCESS_TIP;
    }


    /**
     * 禁用任务
     *
     * @return
     */
    @RequestMapping(value = "/disableJobInfo")
    @ResponseBody
    public Object disableJobInfo(@RequestParam Integer jobInfoId) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        if (jobInfo.getLastRunState()!=null && LastRunState.RUNNING.getCode() == jobInfo.getLastRunState()) {
            throw new GunsException(BizExceptionEnum.JOBINFO_RUN);
        }
        List<JobInfo> icList = jobInfoService.selJobDependByJobId(jobInfoId);
        if (icList.size() > 0 && icList != null) {
            throw new GunsException(BizExceptionEnum.JOBINFOCOF_JOBINFO);
        } else {
            if (jobInfoService.disableJobInfo(jobInfoId) > 0) {

                JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
                try {
                    jobUtil.disableJob(jobInfo.getName());
                    return SUCCESS_TIP;
                } catch (Exception e) {
                    throw new GunsException(SERVER_ERROR);
                }
            }
            return SUCCESS_TIP;
        }
    }

    /**
     * 跳转到运行任务信息
     */
    @RequestMapping("/runJobInfo/{jobInfoId}")
    public String runJobInfo(@PathVariable Integer jobInfoId, Model model) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        model.addAttribute("item", jobInfo);
        return PREFIX + "jobInfo_run.html";
    }

    /**
     * 执行任务信息
     */
    @RequestMapping(value = "/rungoJobInfo")
    @ResponseBody
    public Object rungoJobInfo(JobInfo jobInfo) {
        JobInfo job = jobInfoService.selectById(jobInfo.getId());

        if(job.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        String time_hour = DateTimeKit.formatDateTime(jobInfo.getLastRunTime());

        if (null != job.getLastRunState() && job.getLastRunState() == LastRunState.RUNNING.getCode()) {
            throw new GunsException(BizExceptionEnum.JOBINFO_RUN);
        }
        if (JobStatus.DEACTIVE.getCode() == job.getEnable()) {
            throw new GunsException(BizExceptionEnum.JOBINFO_NOTRUN);
        } else {
            //启用状态

            if (jobInfoService.updateById(job)) {

                JobUtil jobUtil = new JobUtil(jobSetService.selectById(job.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
                try {
                    Map<String, String> params = new HashMap<>();
                    params.put("time_hour", time_hour);
                    jobUtil.runJob(job.getName(), params);
                    //修改最近一次执行状态为运行中
                    job.setLastRunState(LastRunState.RUNNING.getCode());
                    return SUCCESS_TIP;
                } catch (Exception e) {
                    throw new GunsException(SERVER_ERROR);
                }
            }
            return SUCCESS_TIP;
        }
    }

    /**
     * 统计任务信息
     */
    @RequestMapping(value = "/count")
    @ResponseBody
    public int count() {
        return jobInfoService.selectCount(null
        );
    }


    /**
     * 保存数据接入任务配置
     */
    @RequestMapping(value = "/saveData")
    @ResponseBody
    public Object saveInputData(JobConfig jobConfig) {
        JobInfo jobInfo = jobInfoService.selectById(jobConfig.getJobId());
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        //转换特殊字符
        jobConfig.setSql_statment(unescapeHtml(jobConfig.getSql_statment()));
        jobConfig.setInput_input_content(unescapeHtml(jobConfig.getInput_input_content()));
        jobConfig.setOutput_pre_statment(unescapeHtml(jobConfig.getOutput_pre_statment()));

        if(StringUtils.isNotEmpty(jobConfig.getSchedule_depend())){
            //入库的时候依赖前面增加逗号
            StringBuilder sb=new StringBuilder(",");
            for (String jobName:jobConfig.getSchedule_depend().split(",")
                 ) {
                //检测依赖的正确性
                if(jobInfoService.selectCount(new EntityWrapper<JobInfo>().eq("name",jobName).eq("job_set_id",jobInfo.getJobSetId()))==0){
                    throw new GunsException(new BizException(500,"依赖任务不存在"));
                }
                if(StringUtils.isNotEmpty(jobName)){
                    sb.append(jobName+",");
                }
            }
            jobConfig.setSchedule_depend(sb.toString());
        }
        jobInfoConfService.upsertKVByJobId(JobConfig.jobConfigTolist(jobConfig));
        jobInfo.setModPer(ShiroKit.getUser().getId());
        jobInfo.setModTime(DateTimeKit.date());
        JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());

        //job在jenkins里不存在则创建一个
        try {
            if(! jobUtil.ifJobExists(jobInfo.getName())){
                jobUtil.createJob(jobInfo.getName(),"desc","");
            }
            jobUtil.setJobCronTab(jobInfo.getName(),jobConfig.getSchedule_crontab());
            if(StringUtils.isNotEmpty(jobConfig.getSchedule_crontab())) {
                //如果已经有了定时crontab忽略依赖设置
                jobUtil.setDependsJob(jobInfo.getName(), "");
            }else{
                //jenkins中存储的格式不要前后的逗号
                jobUtil.setDependsJob(jobInfo.getName(), jobConfig.getSchedule_depend().replaceFirst(",", ""));
            }


        } catch (Exception e) {
            throw new GunsException(SERVER_ERROR);
        }

        String beelineCmd = hiveConfig.getBeeline()+" "+hiveConfig.getUrl() + jobConfig.getOutput_db_name();
        if (jobInfoService.updateById(jobInfo)) {
            String shell;
            StringBuilder paramCmd=new StringBuilder(String.format("if [ ! -f 'bdp-common-1.0.0.jar' ];then\n" +
                    "\thdfs dfs -get %s/bdp-common-1.0.0.jar .\n" +
                    "fi\n" +
                    "if [ ! -f 'joda-time-2.8.1.jar' ];then\n" +
                    "\thdfs dfs -get %s/joda-time-2.8.1.jar .\n" +
                    "fi\n",bdpJobConfig.getJoblib(),bdpJobConfig.getJoblib()));
            switch (jobInfo.getTypeId()) {

                //数据接入
                case 1: {
                    ConfConnect conf = confConnectService.selectById(jobConfig.getInput_connect_id());
                    ConfConnectType confConnectType = confConnectTypeService.selConfConnectTypeById(conf.getTypeId());
                    Map<String,String> params=JobConfUtil.extraParams(jobConfig.getInput_file_position(),jobConfig.getInput_input_content(),jobConfig.getOutput_data_partition());
                    String configFile = JobConfUtil.genConfigFile(jobConfig,conf,confConnectType,params.keySet());
                    String runCmd="embulk run config.yml ";
                    if(confConnectType.getType().equals(conTypeType.FTP.getCode())){
                        runCmd = "embulk guess -g jsonl config.yml -o guessed.yml && embulk run guessed.yml";

                    }
                    for (String param:params.values()
                         ) {
                        paramCmd.append(param);
                    }
                    shell = String.format("%s\necho \"\"\"\n%s\"\"\" > config.yml\n" +
                            "%s && %s -e 'msck repair table %s.%s'\n", paramCmd.toString(),configFile,runCmd, beelineCmd, jobConfig.getOutput_db_name(), jobConfig.getOutput_table_name());
                    try {
                        jobUtil.setJobCmd(jobInfo.getName(), JobConfUtil.wrapShell(shell,jenkinsConfig));
                    } catch (Exception e) {
                        throw new GunsException(SERVER_ERROR);
                    }
                    break;
                }
                //SQL
                case 2: {
                    Map<String,String> params=JobConfUtil.extraParams(jobConfig.getSql_statment());
                    for (String param:params.values()
                            ) {
                        paramCmd.append(param);
                    }
                    shell = String.format("%s\necho \"\"\"%s\"\"\" > script.hql \n%s -f script.hql"
                            , paramCmd.toString()
                            ,JobConfUtil.replace_date_to_normal(jobConfig.getSql_statment(),params.keySet())
                            ,beelineCmd);
                    try {
                        jobUtil.setJobCmd(jobInfo.getName(), JobConfUtil.wrapShell(shell,jenkinsConfig));
                    } catch (Exception e) {
                        throw new GunsException(SERVER_ERROR);
                    }
                    break;
                }
                //程序执行
                case 3: {
                    Map<String,String> params=JobConfUtil.extraParams(jobConfig.getBase_proc_main_args(),jobConfig.getBase_proc_main_in(),jobConfig.getBase_proc_main_out());
                    for (String param:params.values()
                            ) {
                        paramCmd.append(param);
                    }
                    try {
                        shell=JobConfUtil.gentProcExeShell(jobConfig,jenkinsConfig,bdpJobConfig,params.keySet());
                        jobUtil.setJobCmd(jobInfo.getName(), shell);
                    } catch (Exception e) {
                        throw new GunsException(SERVER_ERROR);
                    }
                    break;
                }
                //数据推送
                case 4: {
                    ConfConnect conf = confConnectService.selectById(jobConfig.getOutput_connect_id());
                    ConfConnectType confConnectType = confConnectTypeService.selConfConnectTypeById(jobConfig.getOutput_connect_type());
                    shell = JobConfUtil.genOutPutShell(jobConfig, conf, confConnectType,bdpJobConfig);
                    try {
                        jobUtil.setJobCmd(jobInfo.getName(), JobConfUtil.wrapShell(shell,jenkinsConfig));
                    } catch (Exception e) {
                        throw new GunsException(SERVER_ERROR);
                    }
                    //上传job配置文件到hdfs上
                    try {
                        JobConfUtil.upLoadJobConf(jobConfig,conf,confConnectType,bdpJobConfig.getNamenodestr(),bdpJobConfig.getJobconfig());
                    } catch (Exception e) {
                        throw new GunsException(new BizException(500,"上传配置文件失败!!!"));
                    }
                    break;
                }
                default:
                    break;
            }
        }

        return SUCCESS_TIP;
    }

    /**
     * 上传图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file,Integer jobId) {
        JobInfo jobInfo = jobInfoService.selectById(jobId);
        if(jobInfo.getUserInfoId()!=ShiroKit.getUser().getId()){
            throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
        }
        if (!file.isEmpty()) {
            try {

                HdfsUtil util=new HdfsUtil(bdpJobConfig.getNamenodestr());
//
//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(new File(file.getOriginalFilename())));
//                out.write(file.getBytes());
                util.writeFile(file.getBytes(),String.format("%s/%s/%s",bdpJobConfig.getJoblib(),jobId,file.getOriginalFilename()));
//                out.flush();
//                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new GunsException(new BizException(500,e.getMessage()));
            }
            return file.getOriginalFilename();
        } else {
            throw new GunsException(new BizException(500,"文件为空!!!"));
        }
    }



    private String unescapeHtml(String str) {
        if (str != null) {
            return StringEscapeUtils.unescapeHtml(str.replaceAll("& ","&"));
        } else {
            return null;
        }
    }
}
