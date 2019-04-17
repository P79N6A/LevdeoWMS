package com.tre.jdevtemplateboot.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JDev
 * @since 2019-01-09
 */
@TableName("stockMoveSpWork")
public class StockMoveSpWork implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行号
     */
    @TableField("rowNum")
    private Integer rowNum;

    /**
     * vin码
     */
    private String vin;

    /**
     * 原库位名
     */
    @TableField("oldPosition")
    private String oldPosition;

    /**
     * 新库位名
     */
    @TableField("newPosition")
    private String newPosition;

    /**
     * 操作人
     */
    private String operator;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }
    public String getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "StockMoveSpWork{" +
        "rowNum=" + rowNum +
        ", vin=" + vin +
        ", oldPosition=" + oldPosition +
        ", newPosition=" + newPosition +
        ", operator=" + operator +
        "}";
    }
}
