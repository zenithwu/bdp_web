package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.ConfConnectType;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 配置连接类型表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface ConfConnectTypeMapper extends BaseMapper<ConfConnectType> {
	public List<ConfConnectType> selectAllConfConnectType();
}
