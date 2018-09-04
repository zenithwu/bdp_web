package com.stylefeng.guns.modular.bdp.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.modular.system.dao.JobInfoMapper;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.model.JobInfoConf;
import com.stylefeng.guns.modular.system.dao.JobInfoConfMapper;
import com.stylefeng.guns.modular.bdp.service.IJobInfoConfService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务信息配置表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@Service
public class JobInfoConfServiceImpl extends ServiceImpl<JobInfoConfMapper, JobInfoConf> implements IJobInfoConfService {

	@Autowired
	private JobInfoConfMapper jobInfoConfMapper;

	@Override
	public List<JobInfoConf> selJobInfoConfByJobInfoId(Integer id) {
		// TODO Auto-generated method stub
		return jobInfoConfMapper.selJobInfoConfByJobInfoId(id);
	}
	@Override
	public void upsertKVByJobId(List<JobInfoConf> jobInfoConfList) {
		jobInfoConfMapper.upsertKVByJobId(jobInfoConfList);
	}


}
