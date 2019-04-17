package com.tre.jdevtemplateboot.web.pojo;

public class OutPDICheckBean {

	private  static final  long serialVersionUID = 1L;

    private String vin;
    
    //a-PDI检测-0合格-1不合格
    private String pdiResult;
    
    //a-检测人
    private String inspector;
    
    //a-不合格理由
    private String reason;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getPdiResult() {
		return pdiResult;
	}

	public void setPdiResult(String pdiResult) {
		this.pdiResult = pdiResult;
	}

	public String getInspector() {
		return inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inspector == null) ? 0 : inspector.hashCode());
		result = prime * result + ((pdiResult == null) ? 0 : pdiResult.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutPDICheckBean other = (OutPDICheckBean) obj;
		if (inspector == null) {
			if (other.inspector != null)
				return false;
		} else if (!inspector.equals(other.inspector))
			return false;
		if (pdiResult == null) {
			if (other.pdiResult != null)
				return false;
		} else if (!pdiResult.equals(other.pdiResult))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OutPDICheckBean [vin=" + vin + ", pdiResult=" + pdiResult + ", inspector=" + inspector + ", reason="
				+ reason + "]";
	}
    
    
    
}
