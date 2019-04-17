package com.tre.jdevtemplateboot.web.pojo;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;

public class OutInvoiceBean extends ParameterBean {


    private  static final  long serialVersionUID = 1L;

    private String sapInvoiceCode;

    private String invoiceDate;
    
    private String customerCode;

    private String customerName;

    private String outType;

    private String outStatus;

	public String getSapInvoiceCode() {
		return sapInvoiceCode;
	}

	public void setSapInvoiceCode(String sapInvoiceCode) {
		this.sapInvoiceCode = sapInvoiceCode;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public String getOutType() {
		return outType;
	}

	public void setOutType(String outType) {
		this.outType = outType;
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

	@Override
	public String toString() {
		return "OutInvoiceBean [sapInvoiceCode=" + sapInvoiceCode + ", invoiceDate=" + invoiceDate + ", customerCode="
				+ customerCode + ", customerName=" + customerName + ", outType=" + outType + ", outStatus=" + outStatus + "]";
	}








}
