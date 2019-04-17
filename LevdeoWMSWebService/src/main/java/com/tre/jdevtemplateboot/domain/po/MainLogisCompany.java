package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 物流公司表
 * </p>
 *
 * @author JDev
 * @since 2019-01-30
 */
@TableName("mainLogisCompany")
public class MainLogisCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物流公司代码
     */
    private String code;

    /**
     * 物流公司名称
     */
    private String name;

    @TableField("createId")
    private String createId;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateId")
    private String updateId;

    @TableField("updateTime")
    private Date updateTime;

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
        return "MainLogisCompany{" +
        "code=" + code +
        ", name=" + name +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}
