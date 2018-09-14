package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.config.properties.HiveConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.HiveUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;

/**
 * 配置连接控制器
 *
 * @author fengshuonan
 * @Date 2018-08-22 09:54:23
 */
@Controller
@RequestMapping("/confConnect")
public class ConfConnectController extends BaseController {

    private String PREFIX = "/bdp/confConnect/";

    @Autowired
    private IConfConnectService confConnectService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IConfConnectTypeService contypeService;
    @Autowired
    private HiveConfig hiveConfig;
    /**
     * 跳转到配置连接首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "confConnect.html";
    }

    /**
     * 跳转到添加配置连接
     */
    @RequestMapping("/confConnect_add")
    public String confConnectAdd() {

        List<ConfConnectType> typeList = contypeService.selectList(null);
        super.setAttr("typeList", typeList);
        return PREFIX + "confConnect_add.html";
    }

    /**
     * 跳转到修改配置连接
     */
    @RequestMapping("/confConnect_update/{confConnectId}")
    public String confConnectUpdate(@PathVariable Integer confConnectId, Model model) {
        ConfConnect confConnect = confConnectService.selectById(confConnectId);
        List<ConfConnectType> typeList = contypeService.selectList(null);
        model.addAttribute("typeList", typeList);
        model.addAttribute("item",confConnect);
        LogObjectHolder.me().set(confConnect);
        return PREFIX + "confConnect_edit.html";
    }

    /**
     * 获取配置连接列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
    	 Wrapper<ConfConnect> wrapper = new EntityWrapper<>();
         wrapper = wrapper.like("name", condition);
         List<ConfConnect> list=confConnectService.selectList(wrapper);
         for (ConfConnect info:list) {
                info.setModPerName(userService.selectById(info.getModPer()).getName());
        	    info.setCreatePerName(userService.selectById(info.getCreatePer()).getName());
            	info.setTypeIdName(contypeService.selectById(info.getTypeId()).getName());                              
         }
         return list;
    }

    /**
     * 新增配置连接
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ConfConnect confConnect) {
        confConnect.setCreatePer(ShiroKit.getUser().getId());
        confConnect.setCreateTime(DateTimeKit.date());
        confConnectService.insert(confConnect);
        return SUCCESS_TIP;
    }

    /**
     * 删除配置连接
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer confConnectId) {
        confConnectService.deleteById(confConnectId);
        return SUCCESS_TIP;
    }

    /**
     * 修改配置连接
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ConfConnect confConnect) {
        confConnect.setModPer(ShiroKit.getUser().getId());
        confConnect.setModTime(DateTimeKit.date());
        confConnectService.updateById(confConnect);
        return SUCCESS_TIP;
    }

    /**
     * 配置连接详情
     */
    @RequestMapping(value = "/detail/{confConnectId}")
    @ResponseBody
    public Object detail(@PathVariable("confConnectId") Integer confConnectId) {
        return confConnectService.selectById(confConnectId);
    }


    /**
     * 根据类型获取类型下的数据源list
     */
    @RequestMapping(value = "/listByTypeId/{confConnectTypeId}")
    @ResponseBody
    public Object listByTypeId(@PathVariable("confConnectTypeId") Integer confConnectTypeId) {
        Wrapper<ConfConnect> wrapper = new EntityWrapper<>();
        wrapper = wrapper.eq("type_id", confConnectTypeId);
        return confConnectService.selectList(wrapper);
    }

    /**
     * 根据类型获取类型下的数据源list
     */
    @RequestMapping(value = "/listHiveTableBydbName/{dbName}")
    @ResponseBody
    public Object listHiveTableBydbName(@PathVariable("dbName") String dbName) {
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        return hiveUtil.getTablesByDbName(dbName);
    }
}
