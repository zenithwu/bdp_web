package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.constant.JobStatus;
import com.stylefeng.guns.core.constant.JobType;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.DateTimeKit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.ConfConnectType;
import com.stylefeng.guns.modular.system.model.JobInfo;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.bdp.service.IConfConnectTypeService;

/**
 * 配置连接类型控制器
 *
 * @author fengshuonan
 * @Date 2018-08-22 09:26:36
 */
@Controller
@RequestMapping("/confConnectType")
public class ConfConnectTypeController extends BaseController {

    private String PREFIX = "/bdp/confConnectType/";

    @Autowired
    private IConfConnectTypeService confConnectTypeService;
    @Autowired
    private IUserService userService;

    /**
     * 跳转到配置连接类型首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "confConnectType.html";
    }

    /**
     * 跳转到添加配置连接类型
     */
    @RequestMapping("/confConnectType_add")
    public String confConnectTypeAdd() {
        return PREFIX + "confConnectType_add.html";
    }

    /**
     * 跳转到修改配置连接类型
     */
    @RequestMapping("/confConnectType_update/{confConnectTypeId}")
    public String confConnectTypeUpdate(@PathVariable Integer confConnectTypeId, Model model) {
        ConfConnectType confConnectType = confConnectTypeService.selectById(confConnectTypeId);
        model.addAttribute("item",confConnectType);
        LogObjectHolder.me().set(confConnectType);
        return PREFIX + "confConnectType_edit.html";
    }

    /**
     * 获取配置连接类型列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<ConfConnectType> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("name", condition);
        List<ConfConnectType> list=confConnectTypeService.selectList(wrapper);
        for (ConfConnectType info:list) {
          
                info.setCreatePerName(userService.selectById(info.getCreatePer()).getName());                                 
        }
        return list;
    }

    /**
     * 新增配置连接类型
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ConfConnectType confConnectType) {
        confConnectType.setCreatePer(ShiroKit.getUser().getId());
        confConnectType.setCreateTime(DateTimeKit.date());
        confConnectTypeService.insert(confConnectType);
        return SUCCESS_TIP;
    }

    /**
     * 删除配置连接类型
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer confConnectTypeId) {
        confConnectTypeService.deleteById(confConnectTypeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改配置连接类型
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ConfConnectType confConnectType) {
        confConnectType.setModPer(ShiroKit.getUser().getId());
        confConnectType.setModTime(DateTimeKit.date());
        confConnectTypeService.updateById(confConnectType);
        return SUCCESS_TIP;
    }

    /**
     * 配置连接类型详情
     */
    @RequestMapping(value = "/detail/{confConnectTypeId}")
    @ResponseBody
    public Object detail(@PathVariable("confConnectTypeId") Integer confConnectTypeId) {
        return confConnectTypeService.selectById(confConnectTypeId);
    }
}
