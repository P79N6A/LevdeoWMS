package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

public class UserParameterBean extends ParameterBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userCode;
	private String userName;
	private String roleName;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "UserParameterBean [userCode=" + userCode + ", userName=" + userName + ", roleName=" + roleName + "]";
	}
	
	
}
