package com.cathay.coindesk.handler;

import com.cathay.coindesk.controller.CoindeskController;
import com.cathay.coindesk.controller.CurrencyController;
import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.rest.RestStatus;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@CommonsLog
@RestControllerAdvice(basePackageClasses = {CoindeskController.class, CurrencyController.class})
public class CustomAuthExceptionHandler {

	/**
	 * 處理自訂的業務邏輯錯誤
	 */
	@ExceptionHandler(ActionException.class)
	public RestResult<Void> handleActionException(ActionException e) {
		log.error("Business error: {}", e);
		return new RestResult<>(e.getErrorCode(),e.getMessage());
	}

	/**
	 * 處理所有未捕獲的錯誤
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResult<Void> handleAllException(Exception e) {
		log.error("Unhandled exception: ", e);
		return new RestResult<>(RestStatus.UNKNOWN,RestStatus.UNKNOWN.MESSAGE);
	}
}
