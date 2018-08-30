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
 * 任务信息表
 * </p>
 *
 * @author zenith
 * @since 2018-08-23
 */
@TableName("job_info")
public class JobInfo extends Model<JobInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务类型 1 数据接入 2 SQL计算 3 程序执行 4 数据推送
     */
    @TableField("type_id")
    private Integer typeId;
    /**
     * 任务描述
     */
    private String desc;
    /**
     * 任务状态
     */
    private Integer enable;
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
    /**
     * 上次运行状态 0 失败 1 成功 2 运行中
     */
    @TableField("last_run_state")
    private Integer lastRunState;
    /**
     * 上次运行时间
     */
    @TableField("last_run_time")
    private Date lastRunTime;
    /**
     * 上次运行耗时 单位ms
     */
    @TableField("last_run_cost")
    private Long lastRunCost;
    /**
     * 所属人id
     */
    @TableField("user_info_id")
    private Integer userInfoId;
    /**
     * 所属任务集
     */
    @TableField("job_set_id")
    private Integer jobSetId;


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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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

    public Integer getLastRunState() {
        return lastRunState;
    }

    public void setLastRunState(Integer lastRunState) {
        this.lastRunState = lastRunState;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Long getLastRunCost() {
        return lastRunCost;
    }

    public void setLastRunCost(Long lastRunCost) {
        this.lastRunCost = lastRunCost;
    }

    public Integer getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Integer getJobSetId() {
        return jobSetId;
    }

    public void setJobSetId(Integer jobSetId) {
        this.jobSetId = jobSetId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "JobInfo{" +
        "id=" + id +
        ", name=" + name +
        ", typeId=" + typeId +
        ", desc=" + desc +
        ", enable=" + enable +
        ", createTime=" + createTime +
        ", createPer=" + createPer +
        ", modTime=" + modTime +
        ", modPer=" + modPer +
        ", lastRunState=" + lastRunState +
        ", lastRunTime=" + lastRunTime +
        ", lastRunCost=" + lastRunCost +
        ", userInfoId=" + userInfoId +
        ", jobSetId=" + jobSetId +
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

    public String getUserInfoName() {
        return userInfoName;
    }

    public void setUserInfoName(String userInfoName) {
        this.userInfoName = userInfoName;
    }

    public String getJobSetName() {
        return jobSetName;
    }

    public void setJobSetName(String jobSetName) {
        this.jobSetName = jobSetName;
    }

    public String getEnableName() {
        return enableName;
    }

    public void setEnableName(String enableName) {
        this.enableName = enableName;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
    
    public String getLastRunStateName() {
		return lastRunStateName;
	}

	public void setLastRunStateName(String lastRunStateName) {
		this.lastRunStateName = lastRunStateName;
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

    /**
     * 所属人id
     */
    @TableField(exist=false)
    private String userInfoName;
    /**
     * 所属任务集
     */
    @TableField(exist=false)
    private String jobSetName;

    /**
     * 任务类型
     */
    @TableField(exist=false)
    private String typeName;

    @TableField(exist=false)
    private String enableName;
    
    /**
     * 最后运行状态
     */
    @TableField(exist=false)
    private String lastRunStateName;
}
