package com.tre.jdevtemplateboot.web.pojo;

import java.util.List;
import java.util.Map;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

/**
 * @description: 盘点计划参数Bean
 * @author: 宋桂帆
 * @create: 2018-12-17
 **/
public class InventoryParameterBean extends ParameterBean {

    private  static final  long serialVersionUID = 1L;
    
    /**
     *	计划单号
     */
    private String planId;
    /**
     * 创建人
     */
    private List<Map<String,Object>> users;

    public List<Map<String,Object>> getUsers() {
		return users;
	}

	public void setUsers(List<Map<String,Object>> users) {
		this.users = users;
	}

	/**
     *	盘点日
     */
    private String planDate;

    public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
     *	状态
     */
    private String status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

	public String getPlanDate() {
		return planDate;
	}

	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
