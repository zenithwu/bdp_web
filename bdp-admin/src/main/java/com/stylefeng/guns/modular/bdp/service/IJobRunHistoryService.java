package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.JobRunHistory;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 任务执行历史表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface IJobRunHistoryService extends IService<JobRunHistory> {

    List<JobRunHistory> selectByParam(String param);

}
