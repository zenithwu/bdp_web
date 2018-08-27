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
import com.stylefeng.guns.modular.system.model.JobStat;
import com.stylefeng.guns.modular.bdp.service.IJobStatService;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-08-27 14:18:51
 */
@Controller
@RequestMapping("/jobStat")
public class JobStatController extends BaseController {

    private String PREFIX = "/bdp/jobStat/";

    @Autowired
    private IJobStatService jobStatService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobStat.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/jobStat_add")
    public String jobStatAdd() {
        return PREFIX + "jobStat_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/jobStat_update/{jobStatId}")
    public String jobStatUpdate(@PathVariable Integer jobStatId, Model model) {
        JobStat jobStat = jobStatService.selectById(jobStatId);
        model.addAttribute("item",jobStat);
        LogObjectHolder.me().set(jobStat);
        return PREFIX + "jobStat_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return jobStatService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobStat jobStat) {
        jobStatService.insert(jobStat);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobStatId) {
        jobStatService.deleteById(jobStatId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobStat jobStat) {
        jobStatService.updateById(jobStat);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{jobStatId}")
    @ResponseBody
    public Object detail(@PathVariable("jobStatId") Integer jobStatId) {
        return jobStatService.selectById(jobStatId);
    }
}
