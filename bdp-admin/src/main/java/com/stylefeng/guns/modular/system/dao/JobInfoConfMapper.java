package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.JobInfoConf;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 任务信息配置表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface JobInfoConfMapper extends BaseMapper<JobInfoConf> {

	/**
	 * 根据任务id查找是否有关联任务
	 * @param id
	 * @return
	 */
	List<JobInfoConf> selJobInfoConfByJobInfoId(Integer id);
	void upsertKVByJobId(List<JobInfoConf> jobInfoConfList);

}
