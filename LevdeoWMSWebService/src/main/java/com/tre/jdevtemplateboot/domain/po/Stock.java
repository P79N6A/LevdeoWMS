package com.tre.jdevtemplateboot.domain.po;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 库存
 * </p>
 *
 * @author JDev
 * @since 2019-01-15
 */
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * vin码
     */
    @TableId("vin")
    private String vin;

    /**
     * 物料编码
     */
    @TableField("matCode")
    private String matCode;

    /**
     * 库位区
     */
    @TableField("stockPosition")
    private String stockPosition;

    /**
     * 入库日期
     */
    @TableField("warehouseDate")
    private Date warehouseDate;

    /**
     * 状态
     */
    private String status;

    /**
     * 锁定-01锁定-02未锁定
     */
    private String lock;

    /**
     * 电池类型
     */
    @TableField("batteryType")
    private String batteryType;

    /**
     * 上次充电日期
     */
    @TableField("lastChargeDate")
    private Date lastChargeDate;

    /**
     * 创建
     */
    @TableField("createId")
    private String createId;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 修改
     */
    @TableField("updateId")
    private String updateId;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 成品库编码
     */
    @TableField("productStockCode")
    private String productStockCode;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }
    public Date getWarehouseDate() {
        return warehouseDate;
    }

    public void setWarehouseDate(Date warehouseDate) {
        this.warehouseDate = warehouseDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }
    public Date getLastChargeDate() {
        return lastChargeDate;
    }

    public void setLastChargeDate(Date lastChargeDate) {
        this.lastChargeDate = lastChargeDate;
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
    public String getProductStockCode() {
        return productStockCode;
    }

    public void setProductStockCode(String productStockCode) {
        this.productStockCode = productStockCode;
    }

    @Override
    public String toString() {
        return "Stock{" +
        "vin=" + vin +
        ", matCode=" + matCode +
        ", stockPosition=" + stockPosition +
        ", warehouseDate=" + warehouseDate +
        ", status=" + status +
        ", lock=" + lock +
        ", batteryType=" + batteryType +
        ", lastChargeDate=" + lastChargeDate +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", productStockCode=" + productStockCode +
        "}";
    }
}
