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
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;

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
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return jobRunHistoryService.selectList(null);
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
        return jobRunHistoryService.selectById(jobRunHistoryId);
    }
}
