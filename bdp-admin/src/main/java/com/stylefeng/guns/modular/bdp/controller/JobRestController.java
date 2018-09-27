package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.stylefeng.guns.config.properties.JenkinsConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.jenkins.JobUtil;
import com.stylefeng.guns.modular.bdp.service.*;
import com.stylefeng.guns.modular.bdp.service.impl.JobInfoServiceImpl;
import com.stylefeng.guns.modular.bdp.service.impl.JobRestServiceImpl;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.model.JobSet;
import com.stylefeng.guns.modular.system.model.JobStat;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 任务信息控制器
 *
 * @author fengshuonan
 * @Date 2018-08-23 18:36:33
 */
@Controller
@RequestMapping("/rest")
public class JobRestController extends BaseController {

    @Autowired
    private IJobRestService jobRestService;


    @RequestMapping(value = "/job_begin", method = RequestMethod.POST)
    @ResponseBody
    public Object job_begin() {
        //jobName zenith/test1     或者 test1
        //number  10 构建编号
        //stat_date 2018-01-01  任务开始执行的时间一般为当前时间
        //params 2018-01-01-01-11
        String jobName = super.getPara("jobName");
        String number = super.getPara("number");
        String stat_date = super.getPara("stat_date");
        String params = super.getPara("params");
        jobRestService.start(jobName, number, stat_date, params);
        return SUCCESS_TIP;
    }



    @RequestMapping(value = "/job_end", method = RequestMethod.POST)
    @ResponseBody
    public Object job_end() {
        //jobName zenith/test1     或者 test1
        //number  10 构建编号
        String jobName = super.getPara("jobName");
        String number = super.getPara("number");
        String stat_date = super.getPara("stat_date");
        jobRestService.end(jobName, number, stat_date);
        return SUCCESS_TIP;
    }




}
