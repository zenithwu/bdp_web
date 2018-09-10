package com.stylefeng.guns.modular.system.model;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务配置的枚举
 *
 * @author zenith
 */
public class JobConfig{


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    private Integer jobId;

    /***
     * 公共模块
     */

    //调度设置_定时执行
    private String schedule_crontab;
    //调度设置_依赖任务
    private String schedule_depend;

    /***
     * 数据接入和推送模块
     */


    //输入设置_数据源连接类型
    private Integer input_connect_type;
    //输入设置_数据源连接
    private Integer input_connect_id;
    //输入设置_输入方式
    private String input_input_type;
    //输入设置_输入内容
    private String input_input_content;

    //输入设置_数据库名称
    private String input_db_name;
    //输入设置_表名称
    private String input_table_name;

    public String getInput_file_position() {
        return input_file_position;
    }

    public void setInput_file_position(String input_file_position) {
        this.input_file_position = input_file_position;
    }

    //输入设置_文件位置
    private String input_file_position;

    //输入设置_数据分区
    private String input_data_partition;


    //输出设置_数据源连接类型
    private Integer output_connect_type;
    //输出设置_数据源连接
    private Integer output_connect_id;
    //输出设置_数据库名称
    private String output_db_name;
    //输出设置_表名称
    private String output_table_name;
    //输出设置_写入模式
    private String output_write_model;
    //输出设置_数据分区
    private String output_data_partition;
    //输出设置_前置语句
    private String output_pre_statment;


    //高级设置_计算资源
    private String  hight_resource;
    //高级设置_线程数
    private String hight_thread;
    //高级设置_文件数
    private String hight_file_num;


    /***
     * SQL计算
     */
    private String sql_statment;


    /***
     * 程序执行
     */
    //程序主类
    private String base_proc_main_class;
    //输入路径
    private String base_proc_main_in;
    //输出路径
    private String base_proc_main_out;
    //程序参数
    private String base_proc_main_args;
    //程序文件地址
    private String base_proc_main_file;

    //spark.driver.memory
    private String  resource_dm;
    //spark.driver.cores
    private String  resource_sc;
    //spark.executor.memory
    private String  resource_em;
    //spark.executor.cores
    private String  resource_ec;

    public String getSchedule_crontab() {
        return schedule_crontab;
    }

    public void setSchedule_crontab(String schedule_crontab) {
        this.schedule_crontab = schedule_crontab;
    }

    public String getSchedule_depend() {
        return schedule_depend;
    }

    public void setSchedule_depend(String schedule_depend) {
        this.schedule_depend = schedule_depend;
    }

    public Integer getInput_connect_id() {
        return input_connect_id;
    }

    public void setInput_connect_id(Integer input_connect_id) {
        this.input_connect_id = input_connect_id;
    }

    public String getInput_input_type() {
        return input_input_type;
    }

    public void setInput_input_type(String input_input_type) {
        this.input_input_type = input_input_type;
    }

    public String getInput_input_content() {
        return input_input_content;
    }

    public void setInput_input_content(String input_input_content) {
        this.input_input_content = input_input_content;
    }

    public String getInput_db_name() {
        return input_db_name;
    }

    public void setInput_db_name(String input_db_name) {
        this.input_db_name = input_db_name;
    }

    public String getInput_table_name() {
        return input_table_name;
    }

    public void setInput_table_name(String input_table_name) {
        this.input_table_name = input_table_name;
    }

    public String getInput_data_partition() {
        return input_data_partition;
    }

    public void setInput_data_partition(String input_data_partition) {
        this.input_data_partition = input_data_partition;
    }

    public Integer getOutput_connect_id() {
        return output_connect_id;
    }

    public void setOutput_connect_id(Integer output_connect_id) {
        this.output_connect_id = output_connect_id;
    }

    public String getOutput_db_name() {
        return output_db_name;
    }

    public void setOutput_db_name(String output_db_name) {
        this.output_db_name = output_db_name;
    }

    public String getOutput_table_name() {
        return output_table_name;
    }

    public void setOutput_table_name(String output_table_name) {
        this.output_table_name = output_table_name;
    }

    public String getOutput_write_model() {
        return output_write_model;
    }

    public void setOutput_write_model(String output_write_model) {
        this.output_write_model = output_write_model;
    }

    public String getOutput_data_partition() {
        return output_data_partition;
    }

    public void setOutput_data_partition(String output_data_partition) {
        this.output_data_partition = output_data_partition;
    }

    public String getOutput_pre_statment() {
        return output_pre_statment;
    }

    public void setOutput_pre_statment(String output_pre_statment) {
        this.output_pre_statment = output_pre_statment;
    }

    public String getHight_resource() {
        return hight_resource;
    }

    public void setHight_resource(String hight_resource) {
        this.hight_resource = hight_resource;
    }

    public String getHight_thread() {
        return hight_thread;
    }

    public void setHight_thread(String hight_thread) {
        this.hight_thread = hight_thread;
    }

    public String getHight_file_num() {
        return hight_file_num;
    }

    public void setHight_file_num(String hight_file_num) {
        this.hight_file_num = hight_file_num;
    }

    public String getSql_statment() {
        return sql_statment;
    }

    public void setSql_statment(String sql_statment) {
        this.sql_statment = sql_statment;
    }

    public String getBase_proc_main_class() {
        return base_proc_main_class;
    }

    public void setBase_proc_main_class(String base_proc_main_class) {
        this.base_proc_main_class = base_proc_main_class;
    }

    public String getBase_proc_main_in() {
        return base_proc_main_in;
    }

    public void setBase_proc_main_in(String base_proc_main_in) {
        this.base_proc_main_in = base_proc_main_in;
    }

    public String getBase_proc_main_out() {
        return base_proc_main_out;
    }

    public void setBase_proc_main_out(String base_proc_main_out) {
        this.base_proc_main_out = base_proc_main_out;
    }

    public String getBase_proc_main_args() {
        return base_proc_main_args;
    }

    public void setBase_proc_main_args(String base_proc_main_args) {
        this.base_proc_main_args = base_proc_main_args;
    }

    public String getBase_proc_main_file() {
        return base_proc_main_file;
    }

    public void setBase_proc_main_file(String base_proc_main_file) {
        this.base_proc_main_file = base_proc_main_file;
    }

    public String getResource_dm() {
        return resource_dm;
    }

    public void setResource_dm(String resource_dm) {
        this.resource_dm = resource_dm;
    }

    public String getResource_sc() {
        return resource_sc;
    }

    public void setResource_sc(String resource_sc) {
        this.resource_sc = resource_sc;
    }

    public String getResource_em() {
        return resource_em;
    }

    public void setResource_em(String resource_em) {
        this.resource_em = resource_em;
    }

    public String getResource_ec() {
        return resource_ec;
    }

    public void setResource_ec(String resource_ec) {
        this.resource_ec = resource_ec;
    }



    public Integer getInput_connect_type() {
        return input_connect_type;
    }

    public void setInput_connect_type(Integer input_connect_type) {
        this.input_connect_type = input_connect_type;
    }

    public Integer getOutput_connect_type() {
        return output_connect_type;
    }

    public void setOutput_connect_type(Integer output_connect_type) {
        this.output_connect_type = output_connect_type;
    }



    public static JobConfig listToJobConfig(List<JobInfoConf> list) {
        if (list == null)
            return null;

        Map<String,Object> map=new HashMap<>();
        for (JobInfoConf conf:list
             ) {
            map.put(conf.getKey(),conf.getValue());
        }
        return mapToJobConfig(map);
    }






    public static List<JobInfoConf> jobConfigTolist(JobConfig jobConfig) {
        if(jobConfig == null)
            return null;

        Map<Object,Object> map= new org.apache.commons.beanutils.BeanMap(jobConfig);
        List<JobInfoConf> list=new ArrayList<>();
        for (Object k:map.keySet()
             ) {

            if(map.get(k) !=null && !("class".equals(k))) {
                JobInfoConf conf = new JobInfoConf();
                conf.setKey(String.valueOf(k));
                conf.setValue(String.valueOf(map.get(k)));
                conf.setJobInfoId(jobConfig.getJobId());
                list.add(conf);
            }
        }
        return  list;

    }


    public static JobConfig mapToJobConfig(Map<String, Object> map) {
        if (map == null)
            return null;


        JobConfig obj;
        try {
            obj = JobConfig.class.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
            return obj;
        } catch (Exception e) {
            return null;
        }
    }




    public static Map<?, ?> jobConfigToMap(JobConfig jobConfig) {
        if(jobConfig == null)
            return null;
        return new org.apache.commons.beanutils.BeanMap(jobConfig);
    }


}
