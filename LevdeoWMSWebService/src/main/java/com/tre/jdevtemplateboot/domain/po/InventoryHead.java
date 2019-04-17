package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 盘点计划
 * </p>
 *
 * @author JDev
 * @since 2018-12-19
 */
@TableName("inventoryHead")
public class InventoryHead implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划单号
     */
    @TableId("planId")
    private String planId;

    /**
     * 盘点日
     */
    @TableField("planDate")
    private Date planDate;

    /**
     * 创建人
     */
    @TableField("createId")
    private String createId;

    /**
     * 创建日
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("updateId")
    private String updateId;

    /**
     * 更新日
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 状态
     */
    private String status;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InventoryHead{" +
        "planId=" + planId +
        ", planDate=" + planDate +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", status=" + status +
        "}";
    }
}
