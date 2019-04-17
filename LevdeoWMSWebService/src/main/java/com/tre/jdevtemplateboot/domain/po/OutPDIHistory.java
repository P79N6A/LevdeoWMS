package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * PDI检测不合格履历
 * </p>
 *
 * @author JDev
 * @since 2019-01-15
 */
@TableName("outPDIHistory")
public class OutPDIHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单单号
     */
    @TableId("invoiceCode")
    private String invoiceCode;

    /**
     * vin码
     */
    private String vin;

    /**
     * 出库单单号
     */
    @TableField("outBoundCode")
    private String outBoundCode;

    /**
     * 检测人
     */
    private String inspector;

    /**
     * 不合格理由
     */
    private String reason;

    @TableField("createId")
    private String createId;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateId")
    private String updateId;

    @TableField("updateTime")
    private Date updateTime;

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
    public String getOutBoundCode() {
        return outBoundCode;
    }

    public void setOutBoundCode(String outBoundCode) {
        this.outBoundCode = outBoundCode;
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
        return "OutPDIHistory{" +
        "invoiceCode=" + invoiceCode +
        ", vin=" + vin +
        ", outBoundCode=" + outBoundCode +
        ", inspector=" + inspector +
        ", reason=" + reason +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}
