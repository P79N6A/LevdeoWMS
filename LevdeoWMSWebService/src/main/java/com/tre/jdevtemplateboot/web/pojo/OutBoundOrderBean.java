package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

import java.util.Arrays;


public class OutBoundOrderBean extends ParameterBean {

    private static final long serialVersionUID = 1L;

    private String outBoundCode;

    private String outBoundDate;

    private String customerCode;

    private String customerName;

    private String outStatus;

    private String paramCode;

    private String confirmStatus;

    private String completeStatus;

    private String[] outStatusList = {};

    public String getOutBoundCode() {
        return outBoundCode;
    }

    public void setOutBoundCode(String outBoundCode) {
        this.outBoundCode = outBoundCode;
    }

    public String getOutBoundDate() {
        return outBoundDate;
    }

    public void setOutBoundDate(String outBoundDate) {
        this.outBoundDate = outBoundDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }

    public String[] getOutStatusList() {
        return outStatusList;
    }

    public void setOutStatusList(String[] outStatusList) {
        this.outStatusList = outStatusList;
    }

    @Override
    public String toString() {
        return "OutBoundOrderBean{" +
                "outBoundCode='" + outBoundCode + '\'' +
                ", outBoundDate='" + outBoundDate + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", outStatus='" + outStatus + '\'' +
                ", paramCode='" + paramCode + '\'' +
                ", confirmStatus='" + confirmStatus + '\'' +
                ", completeStatus='" + completeStatus + '\'' +
                ", billStatus='" + Arrays.toString(outStatusList) + '\'' +
                '}';
    }
}
