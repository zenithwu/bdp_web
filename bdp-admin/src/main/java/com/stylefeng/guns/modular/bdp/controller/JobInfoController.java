package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.constant.JobStatus;
import com.stylefeng.guns.core.constant.JobType;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.bdp.service.IJobSetService;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobSet;
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
        model.addAttribute("item",jobInfo);
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
            if (info.getCreatePer()!=null) {
                info.setCreatePerName(userService.selectById(info.getCreatePer()).getName());
            }
            if (info.getModPer()!=null) {
                info.setModPerName(userService.selectById(info.getModPer()).getName());
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
        jobInfo.setCreatePer(ShiroKit.getUser().getId());
        jobInfo.setCreateTime(DateTimeKit.date());
        jobInfo.setUserInfoId(ShiroKit.getUser().getId());
        //启用状态
        jobInfo.setEnable(0);
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
}
