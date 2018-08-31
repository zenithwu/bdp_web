package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.JobStat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 任务的统计表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface JobStatMapper extends BaseMapper<JobStat> {
	//查询总的成功，失败，进行中
	Map<String,Object> findStatCount();
	Map<String,Object> findStatCountNow();
	
	//查询最近七天数据
	List<Map<String,Object>> findSevenDays();

}
