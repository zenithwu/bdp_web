package com.stylefeng.guns.modular.bdp.service;

import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;
/**
 * <p>
 * 配置连接类型表 服务类
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
public interface IConfConnectTypeService extends IService<ConfConnectType> {
	
	public List<ConfConnectType> selectAllConfConnectType();
}
