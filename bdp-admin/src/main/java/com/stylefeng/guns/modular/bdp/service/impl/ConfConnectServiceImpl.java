package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.stylefeng.guns.modular.system.dao.ConfConnectMapper;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 配置连接表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-22
 */
@Service
public class ConfConnectServiceImpl extends ServiceImpl<ConfConnectMapper, ConfConnect> implements IConfConnectService {
	@Autowired
	private ConfConnectMapper confConnectMapper;

	public List<ConfConnect> selectByJobInfoId(Integer jobInfoId) {

		return confConnectMapper.selectByJobInfoId(jobInfoId);
	}
}
