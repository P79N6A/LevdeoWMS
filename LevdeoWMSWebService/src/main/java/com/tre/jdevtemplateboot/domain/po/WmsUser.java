package com.tre.jdevtemplateboot.domain.po;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2018-12-13
 */
public class WmsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String userName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "WmsUser{" +
        "userId=" + userId +
        ", userName=" + userName +
        "}";
    }
}
