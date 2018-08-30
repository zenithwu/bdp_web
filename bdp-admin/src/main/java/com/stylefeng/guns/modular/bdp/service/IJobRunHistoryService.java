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
	/**
	 * 任务运行添加运行历史
	 * @param jobRunHistory
	 * @return
	 */
	int insertLobRunHistory(JobRunHistory jobRunHistory);
	
	/**
	 * 根据任务id查询任务运行历史信息
	 * @param id
	 * @return
	 */
	List<JobRunHistory> selJobRunHistoryByJobId(int id);
}
