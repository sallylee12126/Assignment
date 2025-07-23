package com.cathay.coindesk.exception.handler;

import com.cathay.coindesk.exception.RestException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.utils.RestExceptionUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@CommonsLog
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler
	public RestResult<Object> handleException(Exception e) {
		log.error("handleException", e);
		RestException restEx = RestExceptionUtils.getRestException(e);
		return new RestResult<>(restEx.getErrorCode(), restEx.getMessage(), restEx.getErrorData());
	}
}
