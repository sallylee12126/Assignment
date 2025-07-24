package com.cathay.coindesk.exception.handler;

import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.exception.RestException;
import com.cathay.coindesk.rest.RestResult;
import com.cathay.coindesk.utils.RestExceptionUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@CommonsLog
@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ActionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResult<Object> handleActionException(ActionException e) {
		log.error("handleActionException", e);
		RestException restEx = RestExceptionUtils.getRestException(e);
		return new RestResult<>(restEx.getErrorCode(), restEx.getMessage(), restEx.getErrorData());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResult<Object> handleValidationException(MethodArgumentNotValidException e) {
		log.error("handleValidationException", e);

		FieldError fieldError = e.getBindingResult().getFieldError();
		String errorMessage = (fieldError != null)
				? fieldError.getField() + " : " + fieldError.getDefaultMessage()
				: "Validation error";

		RestException restEx = new RestException("VF001", errorMessage, null);
		return new RestResult<>(restEx.getErrorCode(), restEx.getMessage(), restEx.getErrorData());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestResult<Object> handleOtherExceptions(Exception e) {
		log.error("handleException", e);
		RestException restEx = RestExceptionUtils.getRestException(e);
		return new RestResult<>(restEx.getErrorCode(), restEx.getMessage(), restEx.getErrorData());
	}
}
