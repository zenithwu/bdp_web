package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobStat;
import com.stylefeng.guns.modular.system.dao.JobStatMapper;
import com.stylefeng.guns.modular.bdp.service.IJobStatService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务的统计表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@Service
public class JobStatServiceImpl extends ServiceImpl<JobStatMapper, JobStat> implements IJobStatService {

	@Override
	public Map<String, Object> findStatCount() {
		// TODO Auto-generated method stub
		return this.baseMapper.findStatCount();
	}
	@Override
	public Map<String, Object> findStatCountNow() {
		// TODO Auto-generated method stub
		return this.baseMapper.findStatCountNow();
	}

	@Override
	public List<Map<String, Object>> findSevenDays() {
		// TODO Auto-generated method stub
		return this.baseMapper.findSevenDays();
	}

	


}
