package com.cathay.coindesk.utils;

import com.cathay.coindesk.constant.SystemId;
import com.cathay.coindesk.error.IErrorCode;
import com.cathay.coindesk.error.SeverityType;
import com.cathay.coindesk.exception.ActionException;

public class CoinDeskUtils {
    public static ActionException newActionException(String errorCode, String errorMsg, Object errorData) {
        return new ActionException(SystemId.COINDESK.SYSTEM_ID, errorCode, SeverityType.ERROR, errorMsg, errorData);
    }

    public static ActionException newActionException(IErrorCode errorCode, Object errorData) {
        return new ActionException(errorCode, errorData);
    }
}
