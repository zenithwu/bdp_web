package com.stylefeng.guns.modular.bdp.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.modular.system.model.JobSet;

/**
 * <p>
 * 任务集信息表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-21
 */
public interface IJobRestService {

     void start(String jobName, String number, String stat_date, String params) ;
     void end(String jobName, String number, String stat_date) ;
     String sync(String jobName, String number) ;
}
