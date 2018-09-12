package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.JobRunHistory;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 任务执行历史表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface JobRunHistoryMapper extends BaseMapper<JobRunHistory> {

	/**
	 * 根据param查询任务运行历史信息
	 * @param param
	 * @return
	 */
	List<JobRunHistory> selectByParam(String param);

}
