package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobSet;
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
    private IJobSetService jobSetService;

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
        return jobSetService.selectList(wrapper);
    }

    /**
     * 新增任务集
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobSet jobSet) {
        jobSet.setCreatePer(ShiroKit.getUser().getId());
        jobSet.setCreateTime(DateTimeKit.date());
        jobSetService.insert(jobSet);
        return SUCCESS_TIP;
    }

    /**
     * 删除任务集
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer jobSetId) {
        jobSetService.deleteById(jobSetId);
        return SUCCESS_TIP;
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
