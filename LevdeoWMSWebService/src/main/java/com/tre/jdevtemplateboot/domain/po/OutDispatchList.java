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
 * @since 2018-12-15
 */
@TableName("outDispatchList")
public class OutDispatchList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单单号
     */
    @TableId("dispatchListNo")
    private String dispatchListNo;

    /**
     * 发货单日期
     */
    @TableField("dispatchListDate")
    private String dispatchListDate;

    /**
     * 送达方
     */
    @TableField("shipToParty")
    private String shipToParty;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 售达方
     */
    @TableField("salesParty")
    private String salesParty;

    /**
     * 销售订单
     */
    @TableField("salesOrder")
    private String salesOrder;

    /**
     * 物流车位
     */
    @TableField("logisticsParking")
    private String logisticsParking;

    /**
     * 送达地址
     */
    @TableField("shipAddress")
    private String shipAddress;

    /**
     * 物流车号
     */
    @TableField("logisticsCarNo")
    private String logisticsCarNo;

    /**
     * 货运方式
     */
    @TableField("shipType")
    private String shipType;

    /**
     * 物料编码
     */
    @TableField("matCode")
    private String matCode;

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

    public String getDispatchListNo() {
        return dispatchListNo;
    }

    public void setDispatchListNo(String dispatchListNo) {
        this.dispatchListNo = dispatchListNo;
    }
    public String getDispatchListDate() {
        return dispatchListDate;
    }

    public void setDispatchListDate(String dispatchListDate) {
        this.dispatchListDate = dispatchListDate;
    }
    public String getShipToParty() {
        return shipToParty;
    }

    public void setShipToParty(String shipToParty) {
        this.shipToParty = shipToParty;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getSalesParty() {
        return salesParty;
    }

    public void setSalesParty(String salesParty) {
        this.salesParty = salesParty;
    }
    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
    }
    public String getLogisticsParking() {
        return logisticsParking;
    }

    public void setLogisticsParking(String logisticsParking) {
        this.logisticsParking = logisticsParking;
    }
    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }
    public String getLogisticsCarNo() {
        return logisticsCarNo;
    }

    public void setLogisticsCarNo(String logisticsCarNo) {
        this.logisticsCarNo = logisticsCarNo;
    }
    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
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

    @Override
    public String toString() {
        return "OutDispatchList{" +
        "dispatchListNo=" + dispatchListNo +
        ", dispatchListDate=" + dispatchListDate +
        ", shipToParty=" + shipToParty +
        ", type=" + type +
        ", status=" + status +
        ", salesParty=" + salesParty +
        ", salesOrder=" + salesOrder +
        ", logisticsParking=" + logisticsParking +
        ", shipAddress=" + shipAddress +
        ", logisticsCarNo=" + logisticsCarNo +
        ", shipType=" + shipType +
        ", matCode=" + matCode +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}
