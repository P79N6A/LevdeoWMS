package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 人员表
 * </p>
 *
 * @author JDev
 * @since 2019-02-22
 */
@TableName("sysUser")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 作业员编号
     */
    @TableId("userCode")
    private String userCode;

    /**
     * 登录名
     */
    @TableField("userName")
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 停用状态，1停用，0使用中
     */
    @TableField("isDel")
    private String isDel;

    /**
     * 角色ID
     */
    @TableField("roleId")
    private String roleId;

    /**
     * 仓库编码
     */
    private String warehouse;

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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
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
        return "SysUser{" +
        "userCode=" + userCode +
        ", userName=" + userName +
        ", password=" + password +
        ", phone=" + phone +
        ", isDel=" + isDel +
        ", roleId=" + roleId +
        ", warehouse=" + warehouse +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", sapUserCode=" + sapUserCode +
        "}";
    }
}
