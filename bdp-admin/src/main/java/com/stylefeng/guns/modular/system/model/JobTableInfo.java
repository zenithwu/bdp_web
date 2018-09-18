package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 表信息
 * </p>
 *
 * @author zenith
 * @since 2018-09-18
 */
@TableName("job_table_info")
public class JobTableInfo extends Model<JobTableInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库名称
     */
    @TableField("db_name")
    private String dbName;
    /**
     * 表名称
     */
    @TableField("table_name")
    private String tableName;
    /**
     * 详情信息
     */
    private String desc;
    @TableField("user_id")
    private Integer userId;


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "JobTableInfo{" +
        "dbName=" + dbName +
        ", tableName=" + tableName +
        ", desc=" + desc +
        ", userId=" + userId +
        "}";
    }
}
