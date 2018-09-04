package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.JobInfoConf;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 任务信息配置表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface IJobInfoConfService extends IService<JobInfoConf> {
	/**
	 * 根据任务id查找是否有关联任务
	 * @param id
	 * @return
	 */
	List<JobInfoConf> selJobInfoConfByJobInfoId(Integer id);

	void upsertKVByJobId(List<JobInfoConf> jobInfoConfList);
}
