package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizException;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.bdp.service.IJobRestService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;
import com.stylefeng.guns.modular.bdp.service.impl.JobRestServiceImpl;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.support.BeanKit;

import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.warpper.LogWarpper;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;

import javax.jws.Oneway;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-08-27 14:48:51
 */
@Controller
@RequestMapping("/jobRunHistory")
public class JobRunHistoryController extends BaseController {

    private String PREFIX = "/bdp/jobRunHistory/";

    @Autowired
    private IJobRunHistoryService jobRunHistoryService;

    @Autowired
    private IJobInfoService jobInfoService;

    @Autowired
    private IJobSetService jobSetService;

    @Autowired
    private IJobRestService jobRestService;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobRunHistory.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/jobRunHistory_add")
    public String jobRunHistoryAdd() {
        return PREFIX + "jobRunHistory_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/jobRunHistory_update/{jobRunHistoryId}")
    public String jobRunHistoryUpdate(@PathVariable Integer jobRunHistoryId, Model model) {
        JobRunHistory jobRunHistory = jobRunHistoryService.selectById(jobRunHistoryId);
        model.addAttribute("item",jobRunHistory);
        LogObjectHolder.me().set(jobRunHistory);
        return PREFIX + "jobRunHistory_edit.html";
    }
    /**
     * 详情
     */
    @RequestMapping("/jobRunHistory_detail/{jobRunHistoryId}")
    public Object jobRunHistoryDetail(@PathVariable Integer jobRunHistoryId, Model model) {
        JobRunHistory jobRunHistory = jobRunHistoryService.selectById(jobRunHistoryId);
        Map<String, Object> stringObjectMap = BeanKit.beanToMap(jobRunHistory);
        return super.warpObject(new LogWarpper(stringObjectMap));
    }
    
    /**
     * 获取列表
     */
    @RequestMapping(value = "/list/{id}")
    @ResponseBody
    public Object list(String condition,@PathVariable Integer id) {

        Wrapper<JobRunHistory> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("params", condition).orderBy("id",false);
        List<JobRunHistory> list;
    	if(id!=null){
            list= jobRunHistoryService.selectList(wrapper.eq("job_info_id",id));
    	}else{
            list= jobRunHistoryService.selectList(wrapper);
    	}
        fillInfo(list);
    	return list;
    }

    /**
     * 从任务页面显示任务运行历史页面
     * @return
     */
    @RequestMapping("/jobRunHistoryList")
    public String jobRunHistoryList() {
        return PREFIX + "jobRunHistory.html";
    }
    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object jobRunHistoryLists(String condition) {
        if(condition==null){
            condition="";
        }
        List<JobRunHistory> list= jobRunHistoryService.selectByParam(condition);
        fillInfo(list);
        return list;
    }

    private void fillInfo(List<JobRunHistory> list) {
        for (JobRunHistory item :list){
            JobInfo info=jobInfoService.selectById(item.getJobInfoId());
            item.setJobName(info.getName());
            if(info.getJobSetId()!=null) {
                JobSet js=jobSetService.selectById(info.getJobSetId());
                if(js!=null) {
                    item.setJobSetName(js.getName());
                }
            }
            item.setStateName(LastRunState.ObjOf(item.getState()).getName());
        }
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobRunHistory jobRunHistory) {
        jobRunHistoryService.insert(jobRunHistory);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobRunHistoryId) {
        jobRunHistoryService.deleteById(jobRunHistoryId);
        return SUCCESS_TIP;
    }



    @RequestMapping(value = "/sync")
    @ResponseBody
    public Object sync(@RequestParam Integer jobRunHistoryId) {

        //job_run_history 根据jobName和构建num更新   statLastRunState   cost log
        JobRunHistory jobRunHistory = jobRunHistoryService.selectById(jobRunHistoryId);
        JobInfo info=jobInfoService.selectById(jobRunHistory.getJobInfoId());
        JobSet set=jobSetService.selectById(info.getJobSetId());
        if (info != null && set!=null) {
            if(info.getUserInfoId()!=ShiroKit.getUser().getId()){
                throw new GunsException(BizExceptionEnum.JOBINFO_PERMISSIOIN);
            }
            String result=jobRestService.sync(String.format("%s/%s",set.getName(),info.getName()),String.valueOf(jobRunHistory.getNum()));
            if(result!=null) {
                return SUCCESS_TIP;
            }else{
                throw new GunsException(new BizException(500,"同步任务信息失败"));
            }
        }else{
            throw new GunsException(new BizException(500,"相关任务不存在"));
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobRunHistory jobRunHistory) {
        jobRunHistoryService.updateById(jobRunHistory);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{jobRunHistoryId}")
    @ResponseBody
    public Object detail(@PathVariable("jobRunHistoryId") Integer jobRunHistoryId) {

        JobRunHistory history=jobRunHistoryService.selectById(jobRunHistoryId);
        //转为浏览器识别的换行符
        history.setLog(history.getLog().replace("\n","</br>"));
        return history;
    }
}
