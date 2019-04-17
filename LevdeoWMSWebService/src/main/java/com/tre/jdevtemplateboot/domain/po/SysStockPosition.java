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
 * @since 2019-01-02
 */
@TableName("sysStockPosition")
public class SysStockPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库位号
     */
    @TableId("stockPosition")
    private String stockPosition;

    /**
     * 库区
     */
    @TableField("stockAreaCode")
    private String stockAreaCode;

    /**
     * 库位名
     */
    private String name;

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
     * 是否被占用-01未使用-02使用中
     */
    @TableField("inUse")
    private String inUse;

    /**
     * 是否锁定-0未锁定-1锁定
     */
    @TableField("isLock")
    private String isLock;

    /**
     * 锁定理由
     */
    @TableField("lockReason")
    private String lockReason;

    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }
    public String getStockAreaCode() {
        return stockAreaCode;
    }

    public void setStockAreaCode(String stockAreaCode) {
        this.stockAreaCode = stockAreaCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
        this.inUse = inUse;
    }
    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }
    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    @Override
    public String toString() {
        return "SysStockPosition{" +
        "stockPosition=" + stockPosition +
        ", stockAreaCode=" + stockAreaCode +
        ", name=" + name +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", inUse=" + inUse +
        ", isLock=" + isLock +
        ", lockReason=" + lockReason +
        "}";
    }
}
