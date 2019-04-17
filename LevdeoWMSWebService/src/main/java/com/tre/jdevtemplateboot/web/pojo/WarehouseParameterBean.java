package com.tre.jdevtemplateboot.web.pojo;


import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class WarehouseParameterBean extends ParameterBean {
    private static final long serialVersionUID = 1L;

    private String wsTransition;

    private String vin;

    private String matCode;


    //@JsonFormat(pattern = "yyyy-MM-dd")
    private String finishedTime;

    private String stockPosition;

    private String matDescription;

    private String state;

    private String stateCode;

    private String sapTransfer;

    private String createId;

    private Date createTime;

    private String updateId;

    private Date updateTime;

    private String productStockCode;

    private String productStockName;

    private String sapTransferCode;
    private List<Map<String,Object>> stockPositions;

    private String batteryType;

    public List<Map<String, Object>> getStockPositions() {
		return stockPositions;
	}

	public void setStockPositions(List<Map<String, Object>> stockPositions) {
		this.stockPositions = stockPositions;
	}

	public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }

    public String getMatDescription() {
        return matDescription;
    }

    public void setMatDescription(String matDescription) {
        this.matDescription = matDescription;
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getProductStockName() {
        return productStockName;
    }

    public void setProductStockName(String productStockName) {
        this.productStockName = productStockName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getSapTransferCode() {
        return sapTransferCode;
    }

    public void setSapTransferCode(String sapTransferCode) {
        this.sapTransferCode = sapTransferCode;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

    @Override
    public String toString() {
        return "WarehouseParameterBean{" +
                "wsTransition='" + wsTransition + '\'' +
                ", vin='" + vin + '\'' +
                ", matCode='" + matCode + '\'' +
                ", finishedTime='" + finishedTime + '\'' +
                ", stockPosition='" + stockPosition + '\'' +
                ", matDescription='" + matDescription + '\'' +
                ", state='" + state + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", sapTransfer='" + sapTransfer + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                ", productStockCode='" + productStockCode + '\'' +
                ", productStockName='" + productStockName + '\'' +
                ", sapTransferCode='" + sapTransferCode + '\'' +
                ", stockPositions=" + stockPositions +
                ", batteryType='" + batteryType + '\'' +
                '}';
    }
}
