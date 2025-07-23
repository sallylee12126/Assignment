package com.cathay.coindesk.error;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;





/**
 * 異常資料
 */
public class ErrorStatus implements Serializable {
	
  
	/**
     * <code>serialVersionUID</code> 的註解
     */
    private static final long serialVersionUID = -8664893608776140109L;

    /** 系統代碼 */
	protected String systemId = "";

	/** 交易回應代碼 */
	protected String errorCode = "";

	/** 
	 * 狀態等級
	 * <ul>
	 * 	<li>ERROR</li>
	 * 	<li>WARNING</li>
	 * 	<li>INFO</li>
	 * 	<li>TIMEOUT</li>
	 * </ul>
	 */
	protected SeverityType severity = SeverityType.INFO;

	/** 狀態描述 */
	protected String errorDesc = "";
 
	public ErrorStatus() {
		super();
	}

	/**
	 * @param sSystemID
	 * @param sStatusCode
	 * @param sSeverity
	 * @param sStatusDesc
	 */
	public ErrorStatus(String systemId, String errorCode, SeverityType severity, String errorDesc) {
		this.systemId = systemId;
		this.errorCode = errorCode;
		this.severity = severity;
		this.errorDesc = errorDesc;
	}

 
 
 
	/**
	 * @see #systemId
	 * @return
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * @see #systemId
	 * @param systemId
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * @see #errorCode
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @see #errorCode
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @see #severity
	 * @return
	 */
	public SeverityType getSeverity() {
		return severity;
	}

	/**
	 * @see #severity
	 * @param severity
	 */
	public void setSeverity(SeverityType severity) {
		this.severity = severity;
	}

	/**
	 * @see #errorDesc
	 * 
	 * @return
	 */
	public String getErrorDesc() {
		return errorDesc;
	}

	/**
	 * @see #errorDesc
	 * @param errorDesc
	 */
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	/**
	 * 是否為Timeout
	 * 
	 * @return
	 */
	public boolean isTimeout() {
		
		return getSeverity().isTimeout();
	}

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
