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
 * 任务的统计表
 * </p>
 *
 * @author zenith
 * @since 2018-08-27
 */
@TableName("job_stat")
public class JobStat extends Model<JobStat> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 统计时间
     */
    @TableField("stat_date")
    private Date statDate;
    /**
     * 成功次数
     */
    private Integer success;
    /**
     * 失败次数
     */
    private Integer fail;
    /**
     * 运行中
     */
    private Integer running;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "JobStat{" +
        "id=" + id +
        ", statDate=" + statDate +
        ", success=" + success +
        ", fail=" + fail +
        ", running=" + running +
        "}";
    }
}
