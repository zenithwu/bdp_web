package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.ProcUtil;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.User;
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

import static com.stylefeng.guns.core.common.exception.BizExceptionEnum.DEPEND_EXISTED;
import static com.stylefeng.guns.core.common.exception.BizExceptionEnum.SERVER_ERROR;

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
    private IJobInfoService jobInfoService;
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
            if(null!=job.getCreatePer()) {
                User user=userService.selectById(job.getCreatePer());
                job.setCreatePerName(user!=null?user.getName():"");
            }
            if(null!=job.getModPer()) {
                User user=userService.selectById(job.getModPer());
                job.setModPerName(user!=null?user.getName():"");
            }
        }
        return list;
    }

    /**
     * 新增任务集
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(JobSet jobSet) {
        JobSet set = jobSetService.selectOne(new EntityWrapper<JobSet>().eq("name",jobSet.getName()));
        if (set != null) {
            throw new GunsException(BizExceptionEnum.JOBSET_EXISTED);
        }
        jobSet.setCreatePer(ShiroKit.getUser().getId());
        jobSet.setCreateTime(DateTimeKit.date());
        if(jobSetService.insert(jobSet)){
            try {
                ProcUtil procUtil=new ProcUtil(jenkinsConfig.getUrl(),jenkinsConfig.getUser(),jenkinsConfig.getToken());
                procUtil.createProject(jobSet.getName());
                return SUCCESS_TIP;
            } catch (IOException e) {
                throw new GunsException(SERVER_ERROR);
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

        if(jobInfoService.selectList(new EntityWrapper<JobInfo>().eq("job_set_id",String.valueOf(jobSetId))).size()>0){
            throw new GunsException(BizExceptionEnum.DEPEND_EXISTED) ;
        }
        JobSet js=jobSetService.selectById(jobSetId);
        if(jobSetService.deleteById(jobSetId)){
            try {
                ProcUtil procUtil=new ProcUtil(jenkinsConfig.getUrl(),jenkinsConfig.getUser(),jenkinsConfig.getToken());
                procUtil.deleteProject(js.getName());
                return SUCCESS_TIP;
            } catch (IOException e) {
                throw new GunsException(SERVER_ERROR);
            }
        }else{
            return SUCCESS_TIP;
        }
    }

    /**
     * 修改任务集
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobSet jobSet) {
        jobSet.setModPer(ShiroKit.getUser().getId());
        jobSet.setModTime(DateTimeKit.date());
        if(jobSetService.updateById(jobSet)) {
            //如果jenkins中没有此工程则添加工程
            try {
                ProcUtil procUtil = new ProcUtil(jenkinsConfig.getUrl(), jenkinsConfig.getUser(), jenkinsConfig.getToken());
                procUtil.createProject(jobSet.getName());
            } catch (IOException e) {

            }
        }

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
