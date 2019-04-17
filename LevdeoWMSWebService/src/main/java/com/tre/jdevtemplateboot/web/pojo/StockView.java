package com.tre.jdevtemplateboot.web.pojo;

import java.util.List;
import java.util.Map;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

public class StockView extends ParameterBean {
	private String name;
	private String stockPosition;
	private String subName;
	private String matCode;
	private String reason;
	private String status;
	private String subCode;
	private String stockArea;
	private String carStatus;
	private String warehouseDate;
	private String vin;
	private List<Map<String,Object>> stockPositions;
	private String stockPositionName;
	private String matDescription;
	private String productStockCode;
	private String wsTransition;

	public String getSubCode() {
		return subCode;
	}
	public void setSubCode(String subCode) {
		this.subCode = subCode;
	}
	public String getStockPositionName() {
		return stockPositionName;
	}
	public void setStockPositionName(String stockPositionName) {
		this.stockPositionName = stockPositionName;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStockPosition() {
		return stockPosition;
	}
	public void setStockPosition(String stockPosition) {
		this.stockPosition = stockPosition;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	public List<Map<String, Object>> getStockPositions() {
		return stockPositions;
	}
	public void setStockPositions(List<Map<String, Object>> stockPositions) {
		this.stockPositions = stockPositions;
	}
	public String getStockArea() {
		return stockArea;
	}
	public void setStockArea(String stockArea) {
		this.stockArea = stockArea;
	}
	public String getCarStatus() {
		return carStatus;
	}
	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}
	public String getWarehouseDate() {
		return warehouseDate;
	}
	public void setWarehouseDate(String warehouseDate) {
		this.warehouseDate = warehouseDate;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getMatDescription() {
		return matDescription;
	}

	public void setMatDescription(String matDescription) {
		this.matDescription = matDescription;
	}

	public String getProductStockCode() {
		return productStockCode;
	}

	public void setProductStockCode(String productStockCode) {
		this.productStockCode = productStockCode;
	}

	public String getWsTransition() {
		return wsTransition;
	}

	public void setWsTransition(String wsTransition) {
		this.wsTransition = wsTransition;
	}

	@Override
	public String toString() {
		return "StockView{" +
				"name='" + name + '\'' +
				", stockPosition='" + stockPosition + '\'' +
				", subName='" + subName + '\'' +
				", matCode='" + matCode + '\'' +
				", reason='" + reason + '\'' +
				", status='" + status + '\'' +
				", subCode='" + subCode + '\'' +
				", stockArea='" + stockArea + '\'' +
				", carStatus='" + carStatus + '\'' +
				", warehouseDate='" + warehouseDate + '\'' +
				", vin='" + vin + '\'' +
				", stockPositions=" + stockPositions +
				", stockPositionName='" + stockPositionName + '\'' +
				", matDescription='" + matDescription + '\'' +
				", productStockCode='" + productStockCode + '\'' +
				", wsTransition='" + wsTransition + '\'' +
				'}';
	}
}
