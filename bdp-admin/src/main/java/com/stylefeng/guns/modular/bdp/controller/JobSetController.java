package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.ProcUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

import com.stylefeng.guns.modular.system.model.JobSet;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;

/**
 * 任务集控制器
 *
 * @author fengshuonan
 * @Date 2018-08-21 14:27:27
 */
@Controller
@RequestMapping("/jobSet")
public class JobSetController extends BaseController {

    private String PREFIX = "/bdp/jobSet/";
    @Autowired
    private JenkinsConfig jenkinsConfig;
    @Autowired
    private IJobSetService jobSetService;
    @Autowired
    private IUserService userService;




    /**
     * 跳转到任务集首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobSet.html";
    }

    /**
     * 跳转到添加任务集
     */
    @RequestMapping("/jobSet_add")
    public String jobSetAdd() {
        return PREFIX + "jobSet_add.html";
    }

    /**
     * 跳转到修改任务集
     */
    @RequestMapping("/jobSet_update/{jobSetId}")
    public String jobSetUpdate(@PathVariable Integer jobSetId, Model model) {
        JobSet jobSet = jobSetService.selectById(jobSetId);
        model.addAttribute("item",jobSet);
        LogObjectHolder.me().set(jobSet);
        return PREFIX + "jobSet_edit.html";
    }

    /**
     * 获取任务集列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<JobSet> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("name", condition);
        List<JobSet> list=jobSetService.selectList(wrapper);
        for (JobSet job:list) {
            	job.setCreatePerName(userService.selectById(job.getCreatePer()).getName());
            	/*job.setModPerName(userService.selectById(job.getModPer()).getName());*/
        }
        return list;
    }

    /**
     * 新增任务集
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobSet jobSet) {
        jobSet.setCreatePer(ShiroKit.getUser().getId());
        jobSet.setCreateTime(DateTimeKit.date());
        if(jobSetService.insert(jobSet)){
            try {
                ProcUtil procUtil=new ProcUtil(jenkinsConfig.getUrl(),jenkinsConfig.getUser(),jenkinsConfig.getToken());
                procUtil.createProject(jobSet.getName());
                return SUCCESS_TIP;
            } catch (IOException e) {
                return new ErrorTip(500,e.getMessage());
            }
        }
        return new ErrorTip(500,"添加失败");
    }

    /**
     * 删除任务集
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobSetId) {

        JobSet js=jobSetService.selectById(jobSetId);
        if(jobSetService.deleteById(jobSetId)){
            try {
                ProcUtil procUtil=new ProcUtil(jenkinsConfig.getUrl(),jenkinsConfig.getUser(),jenkinsConfig.getToken());
                procUtil.deleteProject(js.getName());
                return SUCCESS_TIP;
            } catch (IOException e) {
                return new ErrorTip(500,e.getMessage());
            }
        }
        return new ErrorTip(500,"删除失败");
    }

    /**
     * 修改任务集
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobSet jobSet) {
        jobSet.setModPer(ShiroKit.getUser().getId());
        jobSet.setModTime(DateTimeKit.date());
        jobSetService.updateById(jobSet);
        return SUCCESS_TIP;
    }

    /**
     * 任务集详情
     */
    @RequestMapping(value = "/detail/{jobSetId}")
    @ResponseBody
    public Object detail(@PathVariable("jobSetId") Integer jobSetId) {
        return jobSetService.selectById(jobSetId);
    }
}
