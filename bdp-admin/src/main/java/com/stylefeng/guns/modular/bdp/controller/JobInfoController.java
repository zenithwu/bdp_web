package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.HiveConfig;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.constant.JobStatus;
import com.stylefeng.guns.core.constant.JobType;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.HiveUtil;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IJobInfoService jobInfoService;
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

    /**
     * 跳转到任务信息首页
     */
    @RequestMapping("")
    public String index() {
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
        model.addAttribute("item", jobInfo);
        model.addAttribute("jobConfig", jobConfig);
        ////数据接入 和 数据推送
        if (jobInfo.getTypeId() == JobType.INPUT.getCode() || jobInfo.getTypeId() == JobType.OUTPUT.getCode()) {
            showInputData(model, jobConfig);
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
                Wrapper<ConfConnect> wrapper = new EntityWrapper<>();
                wrapper = wrapper.eq("type_id", connect.getTypeId());
                connects = confConnectService.selectList(wrapper);
            }
        }
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        //查询输出的数据库集合
        List<String> dbList = hiveUtil.getDataBases();
        //查询输出的该数据库的所有的表的集合
        List<String> tableList = new ArrayList<>();
        if (jobConfig.getOutput_db_name() != null) {
            tableList = hiveUtil.getTablesByDbName(jobConfig.getOutput_db_name());
        }
        model.addAttribute("allConfConnectType", allConfConnectType);
        model.addAttribute("connects", connects);
        model.addAttribute("dbList", dbList);
        model.addAttribute("tableList", tableList);
    }

    /**
     * 获取任务信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<JobInfo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("name", condition);

        List<JobInfo> list = jobInfoService.selectList(wrapper);
        for (JobInfo info : list
                ) {
            info.setEnableName(JobStatus.ObjOf(info.getEnable()).getName());
            info.setLastRunStateName(LastRunState.ObjOf(info.getLastRunState()).getName());
            if (info.getCreatePer() != null) {
                info.setCreatePerName(userService.selectById(info.getCreatePer()).getName());
            }
            if (info.getModPer() != null) {
                info.setModPerName(userService.selectById(info.getModPer()).getName());
            }
            if (info.getUserInfoId() != null) {
                info.setUserInfoName(userService.selectById(info.getUserInfoId()).getName());
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
                    jobUtil.createJob(jobInfo.getName(), jobInfo.getDesc(), wrapShell(""));
                    return SUCCESS_TIP;
                } catch (IOException e) {
                    return new ErrorTip(500, e.getMessage());
                }

            }
            return SUCCESS_TIP;
        }
    }

    private String wrapShell(String shell) {
        String begin = "source /etc/profile\n" +
                "param=${time_hour}\n" +
                "stat_date=`date +%Y-%m-%d`\n" +
                "if [ -z '%param' ]; then\n" +
                "param=`date +%Y-%m-%d %H:%M:%S`\n" +
                "fi\n" +
                "curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}&params=${param}\" \"" + jenkinsConfig.getRest_url()+ "/rest/job_begin\"\n" +
                "function run(){\n";
        String end = "\n}\n" +
                "run && (curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}\" \"" + jenkinsConfig.getRest_url() + "/rest/job_end\" &) " +
                "|| (curl -d \"jobName=${JOB_NAME}&number=${BUILD_NUMBER}&stat_date=${stat_date}\" \"" + jenkinsConfig.getRest_url() + "/rest/job_end\" & exit 1)";

        return begin + shell + end;
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
                        return new ErrorTip(500, e.getMessage());
                    }
                }
            }
            return SUCCESS_TIP;
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
        if (jobInfoService.enableJobInfo(jobInfoId) > 0) {
            JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
            try {
                jobUtil.enableJob(jobInfo.getName());
                return SUCCESS_TIP;
            } catch (Exception e) {
                return new ErrorTip(500, e.getMessage());
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
        if (jobInfo.getLastRunState() == LastRunState.RUNNING.getCode()) {
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
                    return new ErrorTip(500, e.getMessage());
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
        String time_hour = DateTimeKit.formatDateTime(jobInfo.getLastRunTime());

        JobInfo job = jobInfoService.selectById(jobInfo.getId());

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
                    return new ErrorTip(500, e.getMessage());
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
        //转换特殊字符
        jobConfig.setSql_statment(unescapeHtml(jobConfig.getSql_statment()));
        jobInfoConfService.upsertKVByJobId(JobConfig.jobConfigTolist(jobConfig));
        JobInfo jobInfo = jobInfoService.selectById(jobConfig.getJobId());
        jobInfo.setModPer(ShiroKit.getUser().getId());
        jobInfo.setModTime(DateTimeKit.date());
        JobUtil jobUtil = new JobUtil(jobSetService.selectById(jobInfo.getJobSetId()).getName(), jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());

        //job在jenkins里不存在则创建一个
        try {
            if(! jobUtil.ifJobExists(jobInfo.getName())){
                jobUtil.createJob(jobInfo.getName(),"desc","");
            }
        } catch (IOException e) {
            return new ErrorTip(500, e.getMessage());
        }

        String beelineCmd = hiveConfig.getBeeline()+" "+hiveConfig.getUrl() + jobConfig.getOutput_db_name();
        if (jobInfoService.updateById(jobInfo)) {
            String shell = "";
            switch (jobInfo.getTypeId()) {

                //数据接入
                case 1: {
                    String configFile = genConfigFile(jobConfig);
                    shell = String.format("echo \"\"\"%s\"\"\" > config.yml\n" +
                            "embulk run config.yml\n" +
                            "%s -e 'msck repair table %s.%s'\n", configFile, beelineCmd, jobConfig.getOutput_db_name(), jobConfig.getOutput_table_name());
                    try {
                        jobUtil.setJobCmd(jobInfo.getName(), wrapShell(shell));
                    } catch (Exception e) {
                        return new ErrorTip(500, e.getMessage());
                    }
                    break;
                }
                //SQL
                case 2: {
                    shell = String.format("echo \"\"\"%s\"\"\" > script.hql \n%s -f script.hql", jobConfig.getSql_statment(),beelineCmd);
                    try {
                        jobUtil.setJobCmd(jobInfo.getName(), wrapShell(shell));
                    } catch (Exception e) {
                        return new ErrorTip(500, e.getMessage());
                    }
                    break;
                }
                //程序执行
                case 3: {


                    break;
                }
                //数据推送
                case 4: {


                    break;
                }
                default:
                    break;
            }
        }

        return SUCCESS_TIP;
    }


    private String genConfigFile(JobConfig jobConfig) {
        ConfConnect conf = confConnectService.selectById(jobConfig.getInput_connect_id());
        String mysql2HDFS = null;

        // sql查询, jobConfig.getInput_input_type().equals("sql")
        String tabAndLoc = genTableLocation(jobConfig);
        mysql2HDFS = String.format("exec: {max_threads: 8, min_output_tasks: 1}\n" +
                        "in:\n" +
                        "  type: mysql\n" +
                        "  driver_path: /opt/cm-5.15.0/share/cmf/lib/mysql-connector-java-5.1.46.jar\n" +
                        "  host: %s\n" +
                        "  port: %s\n" +
                        "  user: %s\n" +
                        "  password: %s\n" +
                        "  database: %s\n" +
                        "  query: %s\n" +
                        "  use_raw_query_with_incremental: false\n" +
                        "  options: {useLegacyDatetimeCode: false, serverTimezone: CST}\n" +
                        "out:\n" +
                        "  type: hdfs\n" +
                        "  config_files: [/etc/hadoop/conf/core-site.xml, /etc/hadoop/conf/hdfs-site.xml]\n" +
                        "  config: {fs.defaultFS: 'hdfs://bdpns', fs.hdfs.impl: org.apache.hadoop.hdfs.DistributedFileSystem,\n" +
                        "    fs.file.impl: org.apache.hadoop.fs.LocalFileSystem}\n" +
                        "  path_prefix: /user/hive/warehouse/%s/\n" +
                        "  file_ext: text\n" +
                        "  mode: %s\n" +
                        "  formatter: {type: jsonl, encoding: UTF-8}",
                conf.getHost(),
                conf.getPort(),
                conf.getUsername(),
                conf.getPassword(),
                conf.getDbname(),
                jobConfig.getInput_input_content(),
                tabAndLoc,
                jobConfig.getOutput_write_model()
        );

        return mysql2HDFS;
    }

    private String genTableLocation(JobConfig jobConfig) {
        String partition = null;
        String tabName = "default".equals(jobConfig.getOutput_db_name()) ? jobConfig.getOutput_table_name() : jobConfig.getOutput_db_name() + ".db/" + jobConfig.getOutput_table_name();

        // partition
        if (jobConfig.getOutput_data_partition() != null) {
            ArrayList<String> arrayList = new ArrayList<String>();

            for (String item : StringUtils.split(jobConfig.getOutput_data_partition(), ",")) {
                StringUtils.trimToNull(item);
                arrayList.add(item);
            }
            partition = StringUtils.join(arrayList.toArray(), "/");
        }
        return (partition == null) ? tabName : tabName + "/" + partition;
    }

    private String unescapeHtml(String str) {
        if (str != null) {
            return StringEscapeUtils.unescapeHtml(str.replaceAll("& #", "&#"));
        } else {
            return null;
        }
    }
}
