package com.stylefeng.guns.modular.bdp.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.config.properties.HiveConfig;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizException;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.constant.LastRunState;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.HiveUtil;
import com.stylefeng.guns.modular.bdp.service.IJobTableInfoService;
import com.stylefeng.guns.modular.system.model.JobInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.JobTableInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据表信息控制器
 *
 * @author fengshuonan
 * @Date 2018-09-18 16:38:07
 */
@Controller
@RequestMapping("/jobTableInfo")
public class JobTableInfoController extends BaseController {

    private String PREFIX = "/bdp/jobTableInfo/";

    @Autowired
    private IJobTableInfoService jobTableInfoService;

    @Autowired
    private HiveConfig hiveConfig;

    /**
     * 跳转到数据表信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobTableInfo.html";
    }

    /**
     * 跳转到添加数据表信息
     */
    @RequestMapping("/jobTableInfo_add")
    public String jobTableInfoAdd() {
        return PREFIX + "jobTableInfo_add.html";
    }

    /**
     * 跳转到修改数据表信息
     */
    @RequestMapping("/jobTableInfo_update/{jobTableInfoId}")
    public String jobTableInfoUpdate(@PathVariable Integer jobTableInfoId, Model model) {
//        JobTableInfo jobTableInfo = jobTableInfoService.selectById(jobTableInfoId);
//        model.addAttribute("item",jobTableInfo);
//        LogObjectHolder.me().set(jobTableInfo);
        return PREFIX + "jobTableInfo_edit.html";
    }

    /**
     * 获取数据表信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl());
        List<JobTableInfo> list=hiveUtil.getTablesBytableName(condition);
        List<JobTableInfo> dbList=jobTableInfoService.selectList(null);
        Map<String,String> dict=new HashMap<>();
        for (JobTableInfo info:dbList
             ) {
            dict.put(info.getDbName()+"_"+info.getTableName(),info.getDesc());
        }
        for (JobTableInfo info:list
             ) {
            info.setDesc(dict.get(info.getDbName()+"_"+info.getTableName()));
        }
        return list;
    }

    /**
     * 新增数据表信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(String  dbName,String tableName,String format,String desc, String dictValues) {
        List<String> cols=new ArrayList<>();
        List<String> pars=new ArrayList<>();
        for (String col:dictValues.split(";")
             ) {
                String[] strs=col.split(":");
                if(strs[2].equals("0")){
                    cols.add(strs[0]+" "+strs[1]);
                }else{
                    pars.add(strs[0]+" "+strs[1]);
                }
        }
        String paritions="";
        if(pars.size()>0){
            paritions=String.format("partitioned by(%s)",StringUtils.join(pars.toArray(),","));
        }
        String statment="";
        if("text".equals(format)){
            statment=String.format("create table %s (\n" +
                    "%s)\n" +
                    "%s",tableName,StringUtils.join(cols.toArray(),","),paritions);
        }
        if("json".equals(format)){
            statment=String.format("create table %s (\n" +
                    "%s)\n" +
                    "%s\n" +
                    "ROW FORMAT SERDE 'org.apache.hive.hcatalog.data.JsonSerDe'\n" +
                    "STORED AS TEXTFILE",tableName,StringUtils.join(cols.toArray(),","),paritions);
        }
        if("parquet".equals(format)){
            statment=String.format("create table %s (\n" +
                    "%s)\n" +
                    "%s\n" +
                    "stored as parquet",tableName,StringUtils.join(cols.toArray(),","),paritions);
        }
        System.out.println(statment);
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl(),dbName);
        try {
            hiveUtil.execute(statment);
            JobTableInfo info=new JobTableInfo();
            info.setDbName(dbName);
            info.setTableName(tableName);
            info.setDesc(desc);
            info.setUserId(ShiroKit.getUser().getId());
            jobTableInfoService.insert(info);
            return SUCCESS_TIP;
        } catch (SQLException e) {
            throw new GunsException(new BizException(500,e.getMessage()));
        }
    }

    /**
     * 删除数据表信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String dbName,@RequestParam String tableName) {
        JobTableInfo info = jobTableInfoService
                .selectOne(new EntityWrapper<JobTableInfo>()
                        .eq("db_name",dbName)
                        .eq("table_name",tableName)
                        .eq("user_id",ShiroKit.getUser().getId()));
        if(info==null){
            throw new GunsException(BizExceptionEnum.TABLE_PERMISSIOIN);
        }
        HiveUtil hiveUtil=new HiveUtil(hiveConfig.getUrl(),dbName);
        try {
            hiveUtil.execute("drop table "+tableName);
            return SUCCESS_TIP;
        } catch (SQLException e) {
            throw new GunsException(new BizException(500,e.getMessage()));
        }
    }

    /**
     * 修改数据表信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(JobTableInfo jobTableInfo) {
//        jobTableInfoService.updateById(jobTableInfo);
        return SUCCESS_TIP;
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

}
