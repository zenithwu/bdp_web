package com.stylefeng.guns.modular.bdp.service.impl;

import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.stylefeng.guns.modular.system.dao.ConfConnectTypeMapper;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * <p>
 * 配置连接类型表 服务实现类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@Service
public class ConfConnectTypeServiceImpl extends ServiceImpl<ConfConnectTypeMapper, ConfConnectType> implements IConfConnectTypeService {
	@Autowired
	private ConfConnectTypeMapper confConnectTypeMapper;

	public List<ConfConnectType> selectAllConfConnectType(){
		return confConnectTypeMapper.selectAllConfConnectType();

	}

	@Override
	public ConfConnectType selConfConnectTypeById(int id) {
		// TODO Auto-generated method stub
		return confConnectTypeMapper.selConfConnectTypeById(id);
	}
}
