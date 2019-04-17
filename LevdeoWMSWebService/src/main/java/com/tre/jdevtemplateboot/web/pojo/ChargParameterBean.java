package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

import java.util.List;
import java.util.Map;

/**
 * @description: 参数Bean
 * @author: JDev
 * @create: 2018-11-22 09:06
 **/
public class ChargParameterBean extends ParameterBean {

    private  static final  long serialVersionUID = 1L;

    private String stockArea;

    private String batteryType;
    
    private String wareHouse;
    
    private List<Map<String,Object>> stockPositions;
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStockArea() {
        return stockArea;
    }

    public void setStockArea(String stockArea) {
        this.stockArea = stockArea;
    }

    public String getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(String batteryType) {
        this.batteryType = batteryType;
    }

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public List<Map<String, Object>> getStockPositions() {
		return stockPositions;
	}

	public void setStockPositions(List<Map<String, Object>> stockPositions) {
		this.stockPositions = stockPositions;
	}
    
    
    
}
