package com.cathay.coindesk.error;

import lombok.AllArgsConstructor;

/**
 * <p>Severity Type</p>
 */
@AllArgsConstructor
public enum SeverityType {
	 
	INFO("INFO", "狀態等級-資訊"),
	WARN("WARN", "狀態等級-警告"),
	ERROR("ERROR", "狀態等級-錯誤"),
	TIMEOUT("TIMEOUT", "狀態等級-逾時"),
	FATAL("FATAL", "狀態等級-異常"),
	UNKNOWN("UNKNOWN", "未知");
 

	/** 代碼 */
	public final String CODE;

	/** 狀態說明* */
	public final String MEMO;
	
	/**
	 * 是否為TIMEOUT
	 * 
	 * @return
	 */
	public boolean isTimeout() {
		return this.equals(SeverityType.TIMEOUT);
	}
	
	/**
	 * 是否為FATAL ERROR
	 * 
	 * @return
	 */
	public boolean isFatal() {
		return equals(SeverityType.FATAL);
	}
	
	/**
	 * 是否為INFO
	 * 
	 * @return
	 */
	public boolean isInfo() {
		return equals(SeverityType.INFO);
	}
	
	/**
	 * 是否為ERROR
	 * 
	 * @return
	 */
	public boolean isError() {
		return equals(SeverityType.ERROR);
	}
}
