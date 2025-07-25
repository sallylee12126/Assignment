package com.cathay.coindesk.error.code;

import com.cathay.coindesk.constant.SystemId;
import com.cathay.coindesk.error.ErrorStatus;
import com.cathay.coindesk.error.IErrorCode;
import com.cathay.coindesk.error.SeverityType;

public enum CoinDeskErrorCode implements IErrorCode {

	CURRENCY_NOT_FOUND("D001","查無幣別資料"),
	CURRENCY_CH_ERROR("D002","查無幣別中文名稱"),
	CURRENCY_ALREADY_EXISTS("D003","該幣別已存在"),
	CURRENCY_RATE_NOT_FOUND("D004","查無匯率"),
	COINDESK_API_ERROR("CD001","COINDESK API錯誤"),
	COINDESK_API_JSON_ERROR("CD002","COINDESK API JSON格式轉換錯誤"),
	COINDESK_TIME_PARSE_ERROR("VA001","CoinDesk API時間資訊轉換失敗")

	;



	/** 異常資料 */
	private ErrorStatus error = null;

	CoinDeskErrorCode(String errorCode, String memo) {
        this(errorCode, memo, SeverityType.ERROR);
    }

	CoinDeskErrorCode(String errorCode, String memo, SeverityType severity) {
        error = new ErrorStatus(SystemId.COINDESK.SYSTEM_ID, errorCode, severity, memo);
    }

	public ErrorStatus getError() {
		return error;
	}
}
