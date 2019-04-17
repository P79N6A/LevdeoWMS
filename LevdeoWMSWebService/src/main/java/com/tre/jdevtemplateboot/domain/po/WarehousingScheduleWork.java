package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 入库交接工作表
 * </p>
 *
 * @author JDev
 * @since 2019-02-22
 */
@TableName("warehousingScheduleWork")
public class WarehousingScheduleWork implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作ID
     */
    @TableField("workId")
    private String workId;

    /**
     * 交接单号
     */
    @TableField("wsTransition")
    private String wsTransition;

    /**
     * 成品库
     */
    @TableField("productStockCode")
    private String productStockCode;

    /**
     * 完工日期
     */
    @TableField("finishedTime")
    private String finishedTime;

    /**
     * 物料编码
     */
    @TableField("matCode")
    private String matCode;

    /**
     * VIN码
     */
    private String vin;

    /**
     * 过账状态
     */
    @TableField("sapTransfer")
    private String sapTransfer;

    /**
     * 取消过账标识
     */
    @TableField("cancelSapTransfer")
    private String cancelSapTransfer;

    @TableField("sapUserCode")
    private String sapUserCode;

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }
    public String getWsTransition() {
        return wsTransition;
    }

    public void setWsTransition(String wsTransition) {
        this.wsTransition = wsTransition;
    }
    public String getProductStockCode() {
        return productStockCode;
    }

    public void setProductStockCode(String productStockCode) {
        this.productStockCode = productStockCode;
    }
    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }
    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getSapTransfer() {
        return sapTransfer;
    }

    public void setSapTransfer(String sapTransfer) {
        this.sapTransfer = sapTransfer;
    }
    public String getCancelSapTransfer() {
        return cancelSapTransfer;
    }

    public void setCancelSapTransfer(String cancelSapTransfer) {
        this.cancelSapTransfer = cancelSapTransfer;
    }
    public String getSapUserCode() {
        return sapUserCode;
    }

    public void setSapUserCode(String sapUserCode) {
        this.sapUserCode = sapUserCode;
    }

    @Override
    public String toString() {
        return "WarehousingScheduleWork{" +
        "workId=" + workId +
        ", wsTransition=" + wsTransition +
        ", productStockCode=" + productStockCode +
        ", finishedTime=" + finishedTime +
        ", matCode=" + matCode +
        ", vin=" + vin +
        ", sapTransfer=" + sapTransfer +
        ", cancelSapTransfer=" + cancelSapTransfer +
        ", sapUserCode=" + sapUserCode +
        "}";
    }
}
