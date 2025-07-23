package com.cathay.coindesk.api.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiRsBody {
	
	/** 系統成功 */
	public static final String SUCCESS = "0000";

	/** 系統錯誤 */
	public static final String UNKNOWN_ERROR = "9999";
	/** 未輸入必填欄位 */
	public static final String REQUIRED_ERROR = "9998";
	/** 格式錯誤 */
	public static final String FORMAT_ERROR = "9997";
	/** 長度錯誤 */
	public static final String LENGTH_ERROR = "9996";
	/** 資料錯誤 */
	public static final String DATA_ERROR = "9995";
	
	/** 訊息代碼 */
	@JsonProperty("StatusCode")
	private String statusCode;

	/** 錯誤訊息 */
	@JsonProperty("ErrorMessage")
	private String errorMessage;
	
	@JsonIgnore
	public boolean isApiSuc() {
		return SUCCESS.equals(statusCode);
	}
}
