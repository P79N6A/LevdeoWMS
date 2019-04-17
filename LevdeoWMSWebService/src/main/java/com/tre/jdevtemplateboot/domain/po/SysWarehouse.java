package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
@TableName("sysWarehouse")
public class SysWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("warehouseCode")
    private String warehouseCode;

    private String name;

    private String address;

    private String contact;

    @TableField("phoneNo")
    private String phoneNo;

    private String remark;

    @TableField("priorityLV1")
    private String priorityLV1;

    @TableField("priorityLV2")
    private String priorityLV2;

    @TableField("priorityLV3")
    private String priorityLV3;

    @TableField("createId")
    private String createId;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateId")
    private String updateId;

    @TableField("updateTime")
    private Date updateTime;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getPriorityLV1() {
        return priorityLV1;
    }

    public void setPriorityLV1(String priorityLV1) {
        this.priorityLV1 = priorityLV1;
    }
    public String getPriorityLV2() {
        return priorityLV2;
    }

    public void setPriorityLV2(String priorityLV2) {
        this.priorityLV2 = priorityLV2;
    }
    public String getPriorityLV3() {
        return priorityLV3;
    }

    public void setPriorityLV3(String priorityLV3) {
        this.priorityLV3 = priorityLV3;
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

    @Override
    public String toString() {
        return "SysWarehouse{" +
        "warehouseCode=" + warehouseCode +
        ", name=" + name +
        ", address=" + address +
        ", contact=" + contact +
        ", phoneNo=" + phoneNo +
        ", remark=" + remark +
        ", priorityLV1=" + priorityLV1 +
        ", priorityLV2=" + priorityLV2 +
        ", priorityLV3=" + priorityLV3 +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}
