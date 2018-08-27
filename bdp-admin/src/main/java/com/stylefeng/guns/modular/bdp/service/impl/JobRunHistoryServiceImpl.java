package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.JobRunHistory;
import com.stylefeng.guns.modular.system.dao.JobRunHistoryMapper;
import com.stylefeng.guns.modular.bdp.service.IJobRunHistoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务执行历史表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@Service
public class JobRunHistoryServiceImpl extends ServiceImpl<JobRunHistoryMapper, JobRunHistory> implements IJobRunHistoryService {

}
