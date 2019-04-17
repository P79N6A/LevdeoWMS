package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2019-02-13
 */
@TableName("sysParm")
public class SysParm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 大分类--1
     */
    private String code;

    /**
     * 名称--状态
     */
    private String name;

    /**
     * 小分类--01
     */
    @TableField("subCode")
    private String subCode;

    /**
     * 名称--待入库
     */
    @TableField("subName")
    private String subName;

    /**
     * 备用
     */
    private Integer bk1;

    /**
     * 备用
     */
    private Integer bk2;

    /**
     * 备用
     */
    private String bk3;

    /**
     * 备用
     */
    private String bk4;

    /**
     * 备用
     */
    private String bk5;

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

    /**
     * 大分类物理名
     */
    private String namep;

    /**
     * 小分类物理名
     */
    @TableField("subNamep")
    private String subNamep;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
    public Integer getBk1() {
        return bk1;
    }

    public void setBk1(Integer bk1) {
        this.bk1 = bk1;
    }
    public Integer getBk2() {
        return bk2;
    }

    public void setBk2(Integer bk2) {
        this.bk2 = bk2;
    }
    public String getBk3() {
        return bk3;
    }

    public void setBk3(String bk3) {
        this.bk3 = bk3;
    }
    public String getBk4() {
        return bk4;
    }

    public void setBk4(String bk4) {
        this.bk4 = bk4;
    }
    public String getBk5() {
        return bk5;
    }

    public void setBk5(String bk5) {
        this.bk5 = bk5;
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
    public String getNamep() {
        return namep;
    }

    public void setNamep(String namep) {
        this.namep = namep;
    }
    public String getSubNamep() {
        return subNamep;
    }

    public void setSubNamep(String subNamep) {
        this.subNamep = subNamep;
    }

    @Override
    public String toString() {
        return "SysParm{" +
        "code=" + code +
        ", name=" + name +
        ", subCode=" + subCode +
        ", subName=" + subName +
        ", bk1=" + bk1 +
        ", bk2=" + bk2 +
        ", bk3=" + bk3 +
        ", bk4=" + bk4 +
        ", bk5=" + bk5 +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", namep=" + namep +
        ", subNamep=" + subNamep +
        "}";
    }
}
