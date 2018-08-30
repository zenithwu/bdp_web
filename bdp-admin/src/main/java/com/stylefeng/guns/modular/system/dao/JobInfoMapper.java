package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.JobInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 任务信息表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
public interface JobInfoMapper extends BaseMapper<JobInfo> {
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
}
