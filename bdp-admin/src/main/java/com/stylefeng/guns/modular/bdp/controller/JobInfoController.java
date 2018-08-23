package com.stylefeng.guns.modular.bdp.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;

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

    @Autowired
    private IJobInfoService jobInfoService;

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
        return PREFIX + "jobInfo_add.html";
    }

    /**
     * 跳转到修改任务信息
     */
    @RequestMapping("/jobInfo_update/{jobInfoId}")
    public String jobInfoUpdate(@PathVariable Integer jobInfoId, Model model) {
        JobInfo jobInfo = jobInfoService.selectById(jobInfoId);
        model.addAttribute("item",jobInfo);
        LogObjectHolder.me().set(jobInfo);
        return PREFIX + "jobInfo_edit.html";
    }

    /**
     * 获取任务信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return jobInfoService.selectList(null);
    }

    /**
     * 新增任务信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobInfo jobInfo) {
        jobInfoService.insert(jobInfo);
        return SUCCESS_TIP;
    }

    /**
     * 删除任务信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobInfoId) {
        jobInfoService.deleteById(jobInfoId);
        return SUCCESS_TIP;
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
     * 任务信息程序执行详情
     */
    @RequestMapping(value = "/detail/3/{jobInfoId}")
    public String detail_3() {
        return  "/bdp/job_detail/proc_exec.html";
    }
}
