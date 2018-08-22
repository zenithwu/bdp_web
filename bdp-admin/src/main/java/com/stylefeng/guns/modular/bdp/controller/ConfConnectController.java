package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ConfConnect;
import com.stylefeng.guns.modular.bdp.service.IConfConnectService;

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
        return PREFIX + "confConnect_add.html";
    }

    /**
     * 跳转到修改配置连接
     */
    @RequestMapping("/confConnect_update/{confConnectId}")
    public String confConnectUpdate(@PathVariable Integer confConnectId, Model model) {
        ConfConnect confConnect = confConnectService.selectById(confConnectId);
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
        return confConnectService.selectList(wrapper);
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
}
