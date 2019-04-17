package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

/**
 * @description: 库位管理参数Bean
 * @author: 宋桂帆
 * @create: 2018-12-17
 **/
public class StockPositionParameterBean extends ParameterBean {

    private static final long serialVersionUID = 1L;

    /**
     * 仓库
     */
    private String warehouse;

    /**
     * 库位
     */
    private String stockArea;
    /**
     * 库位
     */
    private String spUseStatus;
    /**
     * 库位
     */
    private String spLockStatus;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getStockArea() {
        return stockArea;
    }

    public void setStockArea(String stockArea) {
        this.stockArea = stockArea;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSpUseStatus() {
        return spUseStatus;
    }

    public void setSpUseStatus(String spUseStatus) {
        this.spUseStatus = spUseStatus;
    }

    public String getSpLockStatus() {
        return spLockStatus;
    }

    public void setSpLockStatus(String spLockStatus) {
        this.spLockStatus = spLockStatus;
    }
}
