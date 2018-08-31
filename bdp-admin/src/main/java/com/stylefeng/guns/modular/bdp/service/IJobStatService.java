package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.JobStat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 任务的统计表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface IJobStatService extends IService<JobStat> {
	Map<String,Object> findStatCount();
	Map<String,Object> findStatCountNow();
	List<Map<String,Object>> findSevenDays();
}
