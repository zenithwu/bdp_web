package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.dao.JobInfoMapper;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.offbytwo.jenkins.model.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务信息表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
@Service
public class JobInfoServiceImpl extends ServiceImpl<JobInfoMapper, JobInfo> implements IJobInfoService {

	@Autowired
	private JobInfoMapper jobInfoMapper;
	
	
	@Override
	public int enableJobInfo(int id) {
		// TODO Auto-generated method stub
		return jobInfoMapper.enableJobInfo(id);
	}

	@Override
	public int disableJobInfo(int id) {
		// TODO Auto-generated method stub
		return jobInfoMapper.disableJobInfo(id);
	}

	@Override
	public JobInfo selJobInfoByName(String name) {
		// TODO Auto-generated method stub
		return jobInfoMapper.selJobInfoByName(name);
	}

}
