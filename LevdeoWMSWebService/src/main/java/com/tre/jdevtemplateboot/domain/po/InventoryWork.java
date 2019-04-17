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
 * @since 2019-01-14
 */
@TableName("inventoryWork")
public class InventoryWork implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * vin码
     */
    private String vin;

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
     * 盘点人
     */
    @TableField("checkPerson")
    private String checkPerson;

    /**
     * 盘点时间
     */
    @TableField("checkTime")
    private Date checkTime;

    /**
     * 操作员
     */
    private String operator;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "InventoryWork{" +
        "vin=" + vin +
        ", carResult=" + carResult +
        ", keyResult=" + keyResult +
        ", checkPerson=" + checkPerson +
        ", checkTime=" + checkTime +
        ", operator=" + operator +
        "}";
    }
}
