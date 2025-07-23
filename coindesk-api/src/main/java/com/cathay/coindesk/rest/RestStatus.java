
package com.cathay.coindesk.rest;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum RestStatus {

	SUCCESS("200", "OK"),	
	FORBIDDEN("403", "Forbidden"),
	VALID("VF001", "參數檢驗錯誤"),
	EXCEED_MAX_FILE_SIZE("VF002", "超過檔案大小限制"),
	DATABASE_EXCEPTION("VF003", "資料庫存取錯誤"),
	UNKNOWN("9999", "系統錯誤，請稍後再試");

	public final String CODE;
	public final String MESSAGE;
}
