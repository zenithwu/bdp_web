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
 * 配置连接类型表
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@TableName("conf_connect_type")
public class ConfConnectType extends Model<ConfConnectType> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 连接类型名称
     */
    private String name;
    /**
     * 类型编号 0 rdbms 1 ftp
     */
    private Integer type;
    /**
     * 连接类型描述
     */
    private String desc;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * 驱动类
     */
    @TableField("driver_class")
    private String driverClass;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建用户
     */
    @TableField("create_per")
    private Integer createPer;
    /**
     * 修改时间
     */
    @TableField("mod_time")
    private Date modTime;
    /**
     * 修改用户
     */
    @TableField("mod_per")
    private Integer modPer;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatePer() {
        return createPer;
    }

    public void setCreatePer(Integer createPer) {
        this.createPer = createPer;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    public Integer getModPer() {
        return modPer;
    }

    public void setModPer(Integer modPer) {
        this.modPer = modPer;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ConfConnectType{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", desc=" + desc +
        ", createTime=" + createTime +
        ", createPer=" + createPer +
        ", modTime=" + modTime +
        ", modPer=" + modPer +
        "}";
    }
    public String getCreatePerName() {
        return createPerName;
    }

    public void setCreatePerName(String createPerName) {
        this.createPerName = createPerName;
    }
    /**
     * 创建用户
     */
    @TableField(exist=false)
    private String createPerName;

}
