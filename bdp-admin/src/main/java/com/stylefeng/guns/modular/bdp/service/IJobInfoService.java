package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.JobInfo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 任务信息表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
public interface IJobInfoService extends IService<JobInfo> {

	/**
	 * 启用任务
	 * @param id
	 * @return
	 */
	int enableJobInfo(int id);
	
	/**
	 * 禁用任务
	 * @param id
	 * @return
	 */
	int disableJobInfo(int id);
	
	/**
	 * 根据任务名称获取任务信息（任务名不重复）
	 * @param name
	 * @return
	 */
	JobInfo selJobInfoByName(String name);

	/**
	 * 查询引用此任务的任务
	 * @param id
	 * @return
	 */
	public List<JobInfo> selJobDependByJobId(Integer id);
}
