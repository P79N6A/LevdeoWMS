package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 盘点详情表
 * </p>
 *
 * @author JDev
 * @since 2019-02-02
 */
@TableName("inventoryDetails")
public class InventoryDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划单号
     */
    @TableId("planId")
    private String planId;

    /**
     * vin码
     */
    private String vin;

    /**
     * 计划数
     */
    @TableField("planNum")
    private Integer planNum;

    /**
     * 车辆盘点结果
     */
    @TableField("carResult")
    private Integer carResult;

    /**
     * 钥匙盘点结果
     */
    @TableField("keyResult")
    private Integer keyResult;

    /**
     * 计划外车辆 0否 1是
     */
    @TableField("outPlan")
    private String outPlan;

    /**
     * 盘点人
     */
    @TableField("checkPerson")
    private String checkPerson;

    /**
     * 盘点日期
     */
    @TableField("checkTime")
    private Date checkTime;

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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public Integer getPlanNum() {
        return planNum;
    }

    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }
    public Integer getCarResult() {
        return carResult;
    }

    public void setCarResult(Integer carResult) {
        this.carResult = carResult;
    }
    public Integer getKeyResult() {
        return keyResult;
    }

    public void setKeyResult(Integer keyResult) {
        this.keyResult = keyResult;
    }
    public String getOutPlan() {
        return outPlan;
    }

    public void setOutPlan(String outPlan) {
        this.outPlan = outPlan;
    }
    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
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
        return "InventoryDetails{" +
        "planId=" + planId +
        ", vin=" + vin +
        ", planNum=" + planNum +
        ", carResult=" + carResult +
        ", keyResult=" + keyResult +
        ", outPlan=" + outPlan +
        ", checkPerson=" + checkPerson +
        ", checkTime=" + checkTime +
        ", createId=" + createId +
        ", createTime=" + createTime +
        ", updateId=" + updateId +
        ", updateTime=" + updateTime +
        "}";
    }
}
