package com.stylefeng.guns.modular.bdp.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.support.BeanKit;

import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.warpper.LogWarpper;
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
    	System.out.println(id);
    	if(id!=null){
    		return jobRunHistoryService.selJobRunHistoryByJobId(id);
    	}else{
    		return jobRunHistoryService.selectList(null);
    	}
        
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
    public Object jobRunHistoryLists() {
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
