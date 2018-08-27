package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 任务信息配置表
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@TableName("job_info_conf")
public class JobInfoConf extends Model<JobInfoConf> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 配置的键
     */
    private String key;
    /**
     * 配置的值
     */
    private String value;
    /**
     * 任务的编号
     */
    @TableField("job_info_id")
    private Integer jobInfoId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return "JobInfoConf{" +
        "id=" + id +
        ", key=" + key +
        ", value=" + value +
        ", jobInfoId=" + jobInfoId +
        "}";
    }
}
