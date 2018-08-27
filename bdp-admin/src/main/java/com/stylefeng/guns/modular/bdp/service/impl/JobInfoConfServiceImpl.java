package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobInfoConf;
import com.stylefeng.guns.modular.system.dao.JobInfoConfMapper;
import com.stylefeng.guns.modular.bdp.service.IJobInfoConfService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

}
