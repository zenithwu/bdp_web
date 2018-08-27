package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 任务执行历史表
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@TableName("job_run_history")
public class JobRunHistory extends Model<JobRunHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 运行编号
     */
    private Long num;
    /**
     * 运行参数
     */
    private String params;
    /**
     * 运行状态
     */
    private Integer state;
    /**
     * 运行耗时 单位ms
     */
    private Long cost;
    /**
     * 开始运行的时间
     */
    private Date time;
    /**
     * 运行的日志信息
     */
    private String log;
    /**
     * 任务信息编号
     */
    @TableField("job_info_id")
    private Integer jobInfoId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Integer getJobInfoId() {
        return jobInfoId;
    }

    public void setJobInfoId(Integer jobInfoId) {
        this.jobInfoId = jobInfoId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "JobRunHistory{" +
        "id=" + id +
        ", num=" + num +
        ", params=" + params +
        ", state=" + state +
        ", cost=" + cost +
        ", time=" + time +
        ", log=" + log +
        ", jobInfoId=" + jobInfoId +
        "}";
    }
}
