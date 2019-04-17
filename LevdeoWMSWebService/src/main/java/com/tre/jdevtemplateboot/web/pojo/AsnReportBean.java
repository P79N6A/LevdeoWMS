package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

public class AsnReportBean extends ParameterBean {

    private static final long serialVersionUID = 1L;

    private String outBoundCode;

    private String outBoundDateFrom;

    private String outBoundDateTo;

    private String customerCode;

    private String outStatus;

    private String saleCompany;
    private String[] saleCompanyList;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOutBoundCode() {
        return outBoundCode;
    }

    public void setOutBoundCode(String outBoundCode) {
        this.outBoundCode = outBoundCode;
    }

    public String getOutBoundDateFrom() {
        return outBoundDateFrom;
    }

    public void setOutBoundDateFrom(String outBoundDateFrom) {
        this.outBoundDateFrom = outBoundDateFrom;
    }

    public String getOutBoundDateTo() {
        return outBoundDateTo;
    }

    public void setOutBoundDateTo(String outBoundDateTo) {
        this.outBoundDateTo = outBoundDateTo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public String getSaleCompany() {
        return saleCompany;
    }

    public void setSaleCompany(String saleCompany) {
        this.saleCompany = saleCompany;
    }

    public String[] getSaleCompanyList() {
        return saleCompanyList;
    }

    public void setSaleCompanyList(String[] saleCompanyList) {
        this.saleCompanyList = saleCompanyList;
    }
}
