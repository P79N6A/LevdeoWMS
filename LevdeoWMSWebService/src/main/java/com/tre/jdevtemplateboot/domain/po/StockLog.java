package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库存日志
 * </p>
 *
 * @author JDev
 * @since 2019-03-05
 */
@TableName("stockLog")
public class StockLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * vin码
     */
    private String vin;

    /**
     * 物料编码
     */
    @TableField("matCode")
    private String matCode;

    /**
     * 操作code
     */
    @TableField("operateCode")
    private String operateCode;

    /**
     * 原库位号
     */
    @TableField("oldStorageNo")
    private String oldStorageNo;

    /**
     * 新库位号
     */
    @TableField("newStorageNo")
    private String newStorageNo;

    /**
     * 理由
     */
    private String reason;

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
     * sap单号
     */
    @TableField("sapOrderNum")
    private String sapOrderNum;

    /**
     * 借用人、归还人、调拨人姓名
     */
    @TableField("bgdUserName")
    private String bgdUserName;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }
    public String getOldStorageNo() {
        return oldStorageNo;
    }

    public void setOldStorageNo(String oldStorageNo) {
        this.oldStorageNo = oldStorageNo;
    }
    public String getNewStorageNo() {
        return newStorageNo;
    }

    public void setNewStorageNo(String newStorageNo) {
        this.newStorageNo = newStorageNo;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
    public String getSapOrderNum() {
        return sapOrderNum;
    }

    public void setSapOrderNum(String sapOrderNum) {
        this.sapOrderNum = sapOrderNum;
    }
    public String getBgdUserName() {
        return bgdUserName;
    }

    public void setBgdUserName(String bgdUserName) {
        this.bgdUserName = bgdUserName;
    }

    @Override
    public String toString() {
        return "StockLog{" +
                "vin=" + vin +
                ", matCode=" + matCode +
                ", operateCode=" + operateCode +
                ", oldStorageNo=" + oldStorageNo +
                ", newStorageNo=" + newStorageNo +
                ", reason=" + reason +
                ", createId=" + createId +
                ", createTime=" + createTime +
                ", updateId=" + updateId +
                ", updateTime=" + updateTime +
                ", sapOrderNum=" + sapOrderNum +
                ", bgdUserName=" + bgdUserName +
                "}";
    }
}
