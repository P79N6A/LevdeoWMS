package com.tre.jdevtemplateboot.web.pojo;

public class OutUpdateVinBean {

    private static final long serialVersionUID = 1L;

    private String invoiceCode;

    private String oldVIN;

    private String newVIN;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getOldVIN() {
        return oldVIN;
    }

    public void setOldVIN(String oldVIN) {
        this.oldVIN = oldVIN;
    }

    public String getNewVIN() {
        return newVIN;
    }

    public void setNewVIN(String newVIN) {
        this.newVIN = newVIN;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "OutUpdateVinBean [oldVIN=" + oldVIN + ", newVIN=" + newVIN + "]";
    }


}
