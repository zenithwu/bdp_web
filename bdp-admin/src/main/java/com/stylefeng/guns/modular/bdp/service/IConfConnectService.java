package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 配置连接表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-22
 */
public interface IConfConnectService extends IService<ConfConnect> {
	public ConfConnect selectByJobInfoId(Integer jobInfoId);

}
