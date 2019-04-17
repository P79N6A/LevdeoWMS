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
 * @since 2018-12-25
 */
@TableName("sysStockArea")
public class SysStockArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 库区编码
     */
    @TableId("stockAreaCode")
    private String stockAreaCode;

    /**
     * 库区名
     */
    private String name;

    /**
     * 位置
     */
    private String location;

    /**
     * 面积
     */
    private Integer acreage;

    /**
     * 停放数量
     */
    private Integer amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属仓库
     */
    @TableField("warehouseCode")
    private String warehouseCode;

    /**
     * 创建人
     */
    @TableField("createId")
    private String createId;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("updateId")
    private String updateId;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 库区固定临时标志位
     */
    private String flag;

    /**
     * 库位开始编号
     */
    @TableField("startNumber")
    private String startNumber;

    /**
     * 库位结束编号
     */
    @TableField("endNumber")
    private String endNumber;

    public String getStockAreaCode() {
        return stockAreaCode;
    }

    public void setStockAreaCode(String stockAreaCode) {
        this.stockAreaCode = stockAreaCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Integer getAcreage() {
        return acreage;
    }

    public void setAcreage(Integer acreage) {
        this.acreage = acreage;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
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
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }
    public String getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
    }

    @Override
    public String toString() {
        return "SysStockArea{" +
        "stockAreaCode=" + stockAreaCode +
        ", name=" + name +
        ", location=" + location +
        ", acreage=" + acreage +
        ", amount=" + amount +
        ", remark=" + remark +
        ", warehouseCode=" + warehouseCode +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        ", flag=" + flag +
        ", startNumber=" + startNumber +
        ", endNumber=" + endNumber +
        "}";
    }
}
