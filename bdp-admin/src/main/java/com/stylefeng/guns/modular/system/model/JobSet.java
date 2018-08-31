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
 * 任务集信息表
 * </p>
 *
 * @author zenith
 * @since 2018-08-21
 */
@TableName("job_set")
public class JobSet extends Model<JobSet> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 任务集名称
     */
    private String name;
    /**
     * 任务集描述
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
        return "JobSet{" +
        "id=" + id +
        ", name=" + name +
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
    public String getModPerName() {
        return modPerName;
    }

    public void setModPerName(String modPerName) {
        this.modPerName = modPerName;
    }
    /**
     * 创建用户
     */
    @TableField(exist=false)
    private String createPerName;

    /**
     * 修改用户
     */
    @TableField(exist=false)
    private String modPerName;
    
}
