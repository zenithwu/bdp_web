package com.stylefeng.guns.modular.bdp.controller;

import com.stylefeng.guns.config.properties.HiveConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.common.exception.BizException;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.HiveUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobTableInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.stylefeng.guns.core.common.exception.BizExceptionEnum.SERVER_ERROR;

/**
 * 任务数据表控制器
 *
 * @author fengshuonan
 * @Date 2018-09-11 10:36:14
 */
@Controller
@RequestMapping("/jobTableInfo")
public class JobTableInfoController extends BaseController {

    private String PREFIX = "/bdp/jobTableInfo/";

    @Autowired
    private HiveConfig hiveConfig;

    /**
     * 跳转到任务数据表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobTableInfo.html";
    }


    /**
     * 跳转到修改任务数据表
     */
    @RequestMapping("/jobTableInfo_update")
    public String jobTableInfoUpdate() {
        return PREFIX + "jobTableInfo_edit.html";
    }

    /**
     * 获取任务数据表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        return hiveUtil.getTablesBytableName(condition);
    }


    /**
     * 修改任务数据表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobTableInfo jobTableInfo) {

        String script=unescapeHtml(jobTableInfo.getDesc());
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl(),jobTableInfo.getDbName());
        try {
            hiveUtil.execute(script);
            return SUCCESS_TIP;
        } catch (SQLException e) {
            throw new GunsException(new BizException(500,e.getMessage()));
        }
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{dbName}/{tableName}")
    public String detail(@PathVariable("dbName") String dbName,@PathVariable("tableName") String tableName,Model model) throws SQLException {
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        StringBuilder sb=new StringBuilder();
        ResultSet rs=hiveUtil.getTableDescByDbName(dbName,tableName);

        if(rs!=null)
        while (rs.next()){
            sb.append(String.format("<tr class=\"d\"><td>%s</td><td>%s</td><td>%s</td></tr>"
                    ,rs.getString(1),rs.getString(2),rs.getString(3)));
        }


        model.addAttribute("table",sb.toString());
        LogObjectHolder.me().set(sb.toString());
        return PREFIX+"table_detail.html";
    }


    private String unescapeHtml(String str) {
        if (str != null) {
            return StringEscapeUtils.unescapeHtml(str.replaceAll("& #", "&#"));
        } else {
            return null;
        }
    }
}
