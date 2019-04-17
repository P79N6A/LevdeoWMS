package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 发货单表
 * </p>
 *
 * @author JDev
 * @since 2019-03-28
 */
@TableName("outInvoiceHead")
public class OutInvoiceHead implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单单号
     */
    @TableId("invoiceCode")
    private String invoiceCode;

    /**
     * 售达方=客户
     */
    @TableField("customerCode")
    private String customerCode;

    /**
     * 销售订单
     */
    @TableField("salesOrder")
    private String salesOrder;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 送达方
     */
    @TableField("shipToParty")
    private String shipToParty;

    /**
     * 物流车位
     */
    @TableField("logisticsParking")
    private String logisticsParking;

    /**
     * 物流车号
     */
    @TableField("logisticsVehicleNo")
    private String logisticsVehicleNo;

    /**
     * 货运方式
     */
    @TableField("shipType")
    private String shipType;

    /**
     * 随车物料
     */
    @TableField("giftName")
    private String giftName;

    /**
     * sap发货单号
     */
    @TableField("sapInvoiceCode")
    private String sapInvoiceCode;

    /**
     * 物料编码
     */
    @TableField("matCode")
    private String matCode;

    /**
     * 数量
     */
    private String amount;

    /**
     * 物流公司代码
     */
    @TableField("logisCompanyCode")
    private String logisCompanyCode;

    /**
     * 制单人
     */
    @TableField("makeOrderUser")
    private String makeOrderUser;

    /**
     * 制单日期
     */
    @TableField("makeOrderDate")
    private Date makeOrderDate;

    /**
     * 核准人：出库单过账人员
     */
    @TableField("outStockUser")
    private String outStockUser;

    /**
     * 核准日期：出库日期
     */
    @TableField("outStockDate")
    private Date outStockDate;

    /**
     * 订单种类code
     */
    @TableField("orderTypeCode")
    private String orderTypeCode;

    /**
     * 销售公司代码
     */
    @TableField("subCompanyCode")
    private String subCompanyCode;

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
     * 项目号
     */
    @TableField("itemNo")
    private String itemNo;

    /**
     * 宣传品确认状态
     */
    @TableField("proStatus")
    private String proStatus;

    /**
     * 随车配件确认状态
     */
    @TableField("giftStatus")
    private String giftStatus;

    /**
     * 出库日
     */
    @TableField("sendedDate")
    private Date sendedDate;

    /**
     * 宣传品确认人
     */
    @TableField("proChecker")
    private String proChecker;

    /**
     * 配件确认人
     */
    @TableField("giftChecker")
    private String giftChecker;

    /**
     * 成品库编码
     */
    @TableField("productStockCode")
    private String productStockCode;

    @TableField("isPrint")
    private String isPrint;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
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
    public String getShipToParty() {
        return shipToParty;
    }

    public void setShipToParty(String shipToParty) {
        this.shipToParty = shipToParty;
    }
    public String getLogisticsParking() {
        return logisticsParking;
    }

    public void setLogisticsParking(String logisticsParking) {
        this.logisticsParking = logisticsParking;
    }
    public String getLogisticsVehicleNo() {
        return logisticsVehicleNo;
    }

    public void setLogisticsVehicleNo(String logisticsVehicleNo) {
        this.logisticsVehicleNo = logisticsVehicleNo;
    }
    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }
    public String getSapInvoiceCode() {
        return sapInvoiceCode;
    }

    public void setSapInvoiceCode(String sapInvoiceCode) {
        this.sapInvoiceCode = sapInvoiceCode;
    }
    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getLogisCompanyCode() {
        return logisCompanyCode;
    }

    public void setLogisCompanyCode(String logisCompanyCode) {
        this.logisCompanyCode = logisCompanyCode;
    }
    public String getMakeOrderUser() {
        return makeOrderUser;
    }

    public void setMakeOrderUser(String makeOrderUser) {
        this.makeOrderUser = makeOrderUser;
    }
    public Date getMakeOrderDate() {
        return makeOrderDate;
    }

    public void setMakeOrderDate(Date makeOrderDate) {
        this.makeOrderDate = makeOrderDate;
    }
    public String getOutStockUser() {
        return outStockUser;
    }

    public void setOutStockUser(String outStockUser) {
        this.outStockUser = outStockUser;
    }
    public Date getOutStockDate() {
        return outStockDate;
    }

    public void setOutStockDate(Date outStockDate) {
        this.outStockDate = outStockDate;
    }
    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }
    public String getSubCompanyCode() {
        return subCompanyCode;
    }

    public void setSubCompanyCode(String subCompanyCode) {
        this.subCompanyCode = subCompanyCode;
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
    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }
    public String getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(String giftStatus) {
        this.giftStatus = giftStatus;
    }
    public Date getSendedDate() {
        return sendedDate;
    }

    public void setSendedDate(Date sendedDate) {
        this.sendedDate = sendedDate;
    }
    public String getProChecker() {
        return proChecker;
    }

    public void setProChecker(String proChecker) {
        this.proChecker = proChecker;
    }
    public String getGiftChecker() {
        return giftChecker;
    }

    public void setGiftChecker(String giftChecker) {
        this.giftChecker = giftChecker;
    }
    public String getProductStockCode() {
        return productStockCode;
    }

    public void setProductStockCode(String productStockCode) {
        this.productStockCode = productStockCode;
    }
    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    @Override
    public String toString() {
        return "OutInvoiceHead{" +
        "invoiceCode=" + invoiceCode +
        ", customerCode=" + customerCode +
        ", salesOrder=" + salesOrder +
        ", type=" + type +
        ", status=" + status +
        ", shipToParty=" + shipToParty +
        ", logisticsParking=" + logisticsParking +
        ", logisticsVehicleNo=" + logisticsVehicleNo +
        ", shipType=" + shipType +
        ", giftName=" + giftName +
        ", sapInvoiceCode=" + sapInvoiceCode +
        ", matCode=" + matCode +
        ", amount=" + amount +
        ", logisCompanyCode=" + logisCompanyCode +
        ", makeOrderUser=" + makeOrderUser +
        ", makeOrderDate=" + makeOrderDate +
        ", outStockUser=" + outStockUser +
        ", outStockDate=" + outStockDate +
        ", orderTypeCode=" + orderTypeCode +
        ", subCompanyCode=" + subCompanyCode +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", itemNo=" + itemNo +
        ", proStatus=" + proStatus +
        ", giftStatus=" + giftStatus +
        ", sendedDate=" + sendedDate +
        ", proChecker=" + proChecker +
        ", giftChecker=" + giftChecker +
        ", productStockCode=" + productStockCode +
        ", isPrint=" + isPrint +
        "}";
    }
}
