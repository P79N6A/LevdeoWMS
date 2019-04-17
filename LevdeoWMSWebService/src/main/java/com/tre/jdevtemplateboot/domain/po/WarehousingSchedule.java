package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 入库一览表
 * </p>
 *
 * @author JDev
 * @since 2019-02-22
 */
@TableName("warehousingSchedule")
public class WarehousingSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入库交接单号
     */
    @TableField("wsTransition")
    private String wsTransition;

    /**
     * vin
     */
    @TableId("vin")
    private String vin;

    /**
     * 物料编号
     */
    @TableField("matCode")
    private String matCode;

    /**
     * 完工日期
     */
    @TableField("finishedTime")
    private Date finishedTime;

    /**
     * 库位号
     */
    @TableField("stockPosition")
    private String stockPosition;

    /**
     * 车辆状态
     */
    private String state;

    /**
     * sap过账
     */
    @TableField("sapTransfer")
    private String sapTransfer;

    /**
     * 成品库编码
     */
    @TableField("productStockCode")
    private String productStockCode;

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

    @TableField("sapUserCode")
    private String sapUserCode;

    public String getWsTransition() {
        return wsTransition;
    }

    public void setWsTransition(String wsTransition) {
        this.wsTransition = wsTransition;
    }
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
    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }
    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getSapTransfer() {
        return sapTransfer;
    }

    public void setSapTransfer(String sapTransfer) {
        this.sapTransfer = sapTransfer;
    }
    public String getProductStockCode() {
        return productStockCode;
    }

    public void setProductStockCode(String productStockCode) {
        this.productStockCode = productStockCode;
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
    public String getSapUserCode() {
        return sapUserCode;
    }

    public void setSapUserCode(String sapUserCode) {
        this.sapUserCode = sapUserCode;
    }

    @Override
    public String toString() {
        return "WarehousingSchedule{" +
        "wsTransition=" + wsTransition +
        ", vin=" + vin +
        ", matCode=" + matCode +
        ", finishedTime=" + finishedTime +
        ", stockPosition=" + stockPosition +
        ", state=" + state +
        ", sapTransfer=" + sapTransfer +
        ", productStockCode=" + productStockCode +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", sapUserCode=" + sapUserCode +
        "}";
    }
}
