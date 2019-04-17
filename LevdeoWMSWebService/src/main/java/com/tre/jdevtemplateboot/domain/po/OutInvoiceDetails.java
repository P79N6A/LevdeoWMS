package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 出库单详情表
 * </p>
 *
 * @author JDev
 * @since 2019-04-16
 */
@TableName("outInvoiceDetails")
public class OutInvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单单号
     */
    @TableId("invoiceCode")
    private String invoiceCode;

    /**
     * VIN
     */
    private String vin;

    /**
     * PDI检测-0合格-1不合格
     */
    private String pdi;

    /**
     * 检测人
     */
    private String inspector;

    /**
     * 不合格理由
     */
    private String reason;

    /**
     * 库位名称
     */
    @TableField("stockPositionName")
    private String stockPositionName;

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
     * 司机
     */
    @TableField("driverId")
    private String driverId;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getPdi() {
        return pdi;
    }

    public void setPdi(String pdi) {
        this.pdi = pdi;
    }
    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getStockPositionName() {
        return stockPositionName;
    }

    public void setStockPositionName(String stockPositionName) {
        this.stockPositionName = stockPositionName;
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
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "OutInvoiceDetails{" +
        "invoiceCode=" + invoiceCode +
        ", vin=" + vin +
        ", pdi=" + pdi +
        ", inspector=" + inspector +
        ", reason=" + reason +
        ", stockPositionName=" + stockPositionName +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", driverId=" + driverId +
        "}";
    }
}
