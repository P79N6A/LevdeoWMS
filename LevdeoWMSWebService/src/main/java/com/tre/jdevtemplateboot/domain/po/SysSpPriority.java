package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2018-12-25
 */
@TableName("sysSpPriority")
public class SysSpPriority implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库位号
     */
    @TableId("stockPosition")
    private String stockPosition;

    /**
     * 优先级CODE，品牌，系列，颜色
     */
    @TableField("priorityCode")
    private String priorityCode;

    /**
     * 具体品牌，系列，颜色的CODE
     */
    @TableField("prioritySubCode")
    private String prioritySubCode;

    /**
     * 创建人
     */
    @TableField("createId")
    private String createId;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("updateId")
    private String updateId;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 优先级
     */
    @TableField("priorityId")
    private Integer priorityId;

    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }
    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }
    public String getPrioritySubCode() {
        return prioritySubCode;
    }

    public void setPrioritySubCode(String prioritySubCode) {
        this.prioritySubCode = prioritySubCode;
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
    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    @Override
    public String toString() {
        return "SysSpPriority{" +
        "stockPosition=" + stockPosition +
        ", priorityCode=" + priorityCode +
        ", prioritySubCode=" + prioritySubCode +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", priorityId=" + priorityId +
        "}";
    }
}
