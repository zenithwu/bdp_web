package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 配置连接表 Mapper 接口
 * </p>
 *
 * @author zenith
 * @since 2018-08-22
 */
public interface ConfConnectMapper extends BaseMapper<ConfConnect> {
	public ConfConnect selectByJobInfoId(Integer jobInfoId);
}
