package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.dao.JobInfoMapper;
import com.stylefeng.guns.modular.bdp.service.IJobInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

}
