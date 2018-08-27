package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobStat;
import com.stylefeng.guns.modular.system.dao.JobStatMapper;
import com.stylefeng.guns.modular.bdp.service.IJobStatService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

}
