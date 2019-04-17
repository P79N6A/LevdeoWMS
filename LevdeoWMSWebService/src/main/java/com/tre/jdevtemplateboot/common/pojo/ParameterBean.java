package com.tre.jdevtemplateboot.common.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description: 参数Bean
 * @author: JDev
 * @create: 2018-11-22 09:06
 **/
public class ParameterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //开始数据行号
    private Integer start;

    //每页显示数据个数
    private Integer limit;

    //当前页
    private Integer currentPage;

    //排序列名
    private String orderColumn;

    //升序、降序
    private String orderDir;

    private String userCode;
    private String warehouse;
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

	
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
    
}
