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
 * 配置连接表
 * </p>
 *
 * @author zenith
 * @since 2018-08-22
 */
@TableName("conf_connect")
public class ConfConnect extends Model<ConfConnect> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 连接名称
     */
    private String name;
    /**
     * 连接类型
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库名称
     */
    private String dbname;
    /**
     * 描述
     */
    private String desc;
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
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
        return "ConfConnect{" +
        "id=" + id +
        ", name=" + name +
        ", typeId=" + typeId +
        ", host=" + host +
        ", port=" + port +
        ", username=" + username +
        ", password=" + password +
        ", dbname=" + dbname +
        ", desc=" + desc +
        ", createTime=" + createTime +
        ", createPer=" + createPer +
        ", modTime=" + modTime +
        ", modPer=" + modPer +
        "}";
    }
}
