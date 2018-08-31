package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.constant.JobStatus;
import com.stylefeng.guns.core.constant.JobType;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;
import com.stylefeng.guns.modular.bdp.service.IJobInfoConfService;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    private String DETAIL="/bdp/job_detail/";

    @Autowired
    private IJobInfoService jobInfoService;
    @Autowired
    private IJobSetService jobSetService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IJobInfoConfService jobInfoConfService;
    @Autowired
    private IJobRunHistoryService jobRunHistoryService;
    @Autowired
    private IConfConnectService confConnectService;
    @Autowired
    private IConfConnectTypeService confConnectTypeService;
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
        super.setAttr("jobSetList",jobSets);
        return PREFIX + "jobInfo_add.html";
    }

    /**
     * 跳转到修改任务信息
     */
    @RequestMapping("/jobInfo_update/{jobInfoId}")
    public String jobInfoUpdate(@PathVariable Integer jobInfoId, Model model) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        List<JobInfoConf> jobInfoConfList = jobInfoConfService.selJobInfoConfByJobInfoId(jobInfoId);
        ConfConnect confConnect = confConnectService.selectByJobInfoId(jobInfoId);
        List<ConfConnectType> allConfConnectType = confConnectTypeService.selectAllConfConnectType();
        
        model.addAttribute("item",jobInfo);
        model.addAttribute("jobInfoConfList",jobInfoConfList);
        model.addAttribute("confConnect",confConnect);
        model.addAttribute("allConfConnectType",allConfConnectType);
        LogObjectHolder.me().set(jobInfo);
        String pageName=JobType.ObjOf(jobInfo.getTypeId()).getPage();
        return DETAIL + pageName;
    }

    /**
     * 获取任务信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<JobInfo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("name", condition);

        List<JobInfo> list=jobInfoService.selectList(wrapper);
        for (JobInfo info:list
             ) {
            info.setEnableName(JobStatus.ObjOf(info.getEnable()).getName());
            info.setLastRunStateName(LastRunState.ObjOf(info.getLastRunState()).getName());
            if (info.getCreatePer()!=null) {
                info.setCreatePerName(userService.selectById(info.getCreatePer()).getName());
            }
            if (info.getModPer()!=null) {
                info.setModPerName(userService.selectById(info.getModPer()).getName());
            }
            if (info.getUserInfoId()!=null) {
                info.setUserInfoName(userService.selectById(info.getUserInfoId()).getName());
            }
            if (info.getJobSetId()!=null) {
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
       if(job != null){
    	   throw new GunsException(BizExceptionEnum.JOBINFO_EXISTED);
       }else{
    	   jobInfo.setCreatePer(ShiroKit.getUser().getId());
           jobInfo.setCreateTime(DateTimeKit.date());
           jobInfo.setUserInfoId(ShiroKit.getUser().getId());
           //启用状态
           jobInfo.setEnable(0);
           jobInfoService.insert(jobInfo);
           return SUCCESS_TIP;
       }
    }

    /**
     * 删除任务信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobInfoId) {
       List<JobInfoConf> icList = jobInfoConfService.selJobInfoConfByJobInfoId(jobInfoId);
        if(icList.size()>0 && icList != null){
        	throw new GunsException(BizExceptionEnum.JOBINFOCOF_JOBINFO);
        }else{
        	jobInfoService.deleteById(jobInfoId);
        	return SUCCESS_TIP;
        }
    }

    /**
     * 修改任务信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobInfo jobInfo) {
        jobInfoService.updateById(jobInfo);
        return SUCCESS_TIP;
    }
    
    /**
     * 启用任务
     * @return
     */
    @RequestMapping(value = "/enableJobInfo")
    @ResponseBody
    public Object enableJobInfo(@RequestParam Integer jobInfoId){
        jobInfoService.enableJobInfo(jobInfoId);
        return SUCCESS_TIP;
    }
    
    
    /**
     * 禁用任务
     * @return
     */
    @RequestMapping(value = "/disableJobInfo")
    @ResponseBody
    public Object disableJobInfo(@RequestParam Integer jobInfoId){
    	List<JobInfoConf> icList = jobInfoConfService.selJobInfoConfByJobInfoId(jobInfoId);
        if(icList.size()>0 && icList != null){
        	throw new GunsException(BizExceptionEnum.JOBINFOCOF_JOBINFO);
        }else{
        	jobInfoService.disableJobInfo(jobInfoId);
        	return SUCCESS_TIP;
        }
    }
    
    /**
     * 跳转到运行任务信息
     */
    @RequestMapping("/runJobInfo/{jobInfoId}")
    public String runJobInfo(@PathVariable Integer jobInfoId, Model model) {
    	 JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
         model.addAttribute("item",jobInfo);
        return PREFIX + "jobInfo_run.html";
    }
    
    /**
     * 新增任务信息
     */
    @RequestMapping(value = "/rungoJobInfo")
    @ResponseBody
    public Object rungoJobInfo(JobInfo jobInfo) {

        JobInfo job=jobInfoService.selectById(jobInfo.getId());

        if(JobStatus.DEACTIVE.getCode()==job.getEnable()){
            throw new GunsException(BizExceptionEnum.JOBINFO_NOTRUN);
        }else {
            //启用状态
            job.setLastRunState(LastRunState.RUNNING.getCode());
            jobInfoService.updateById(job);
            return SUCCESS_TIP;
        }
    }

}
