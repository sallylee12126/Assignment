package com.cathay.coindesk.utils;

import com.cathay.coindesk.exception.ActionException;
import com.cathay.coindesk.exception.DatabaseException;
import com.cathay.coindesk.exception.RestException;
import com.cathay.coindesk.rest.RestStatus;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@CommonsLog
public class RestExceptionUtils {

	/**
	 * 處理未知的 Exception
	 * 
	 * @param t
	 * @return
	 */
	public static RestException getRestException(Throwable t) {
		if (log.isTraceEnabled()) {
			log.trace("converting exception:", t);
		}
		Locale userLocale = ContextUtils.getLocale();
		return getRestException(t, userLocale);
	}

	public static RestException getRestException(Throwable t, Locale userLocale) {
		if (t instanceof InvocationTargetException) {
			return getRestException(((InvocationTargetException) t).getTargetException());
		} else if (t instanceof RestException) {
			// check message here
			RestException aex = (RestException) t;
			return aex;
		} else if(t instanceof ActionException) {
			return getRestException((ActionException) t);
		} else if (t instanceof DatabaseException) {
			return getRestException((DatabaseException) t);
		} else if (t instanceof MissingServletRequestParameterException) {
			return getRestException((MissingServletRequestParameterException) t);
		} else if (t instanceof MethodArgumentNotValidException) {
			return getRestException((MethodArgumentNotValidException) t);
		} else if (t instanceof ConstraintViolationException) {
			return getRestException((ConstraintViolationException) t);
		} else if (t instanceof HttpMessageNotReadableException) {
			return getRestException((HttpMessageNotReadableException) t);
		} else if(t instanceof MissingServletRequestPartException){
			return 	getRestException((MissingServletRequestPartException)t);
		} else if(t instanceof MaxUploadSizeExceededException){
			return 	getRestException((MaxUploadSizeExceededException)t);
		} else if(t instanceof ValidationException){
			return 	getRestException((ValidationException)t);
		}

		return getRestException(new RestException(RestStatus.UNKNOWN, t));
	}
	
	public static RestException getRestException(DatabaseException e) {
		return getRestException(new RestException(RestStatus.DATABASE_EXCEPTION, e));
	}

	public static RestException getRestException(ActionException e) {
		return getRestException(new RestException(e.getErrorCode(), e.getMessage(), e.getData(), e));
	}
	
	public static RestException getRestException(MissingServletRequestParameterException e) {
		return getRestException(new RestException(RestStatus.VALID.CODE, e.getParameterName() + " : " + e.getMessage(), e));
	}

	public static RestException getRestException(MethodArgumentNotValidException e) {
		return getRestException(new RestException(RestStatus.VALID.CODE,
				e.getFieldError().getField() + " : " + e.getFieldError().getDefaultMessage(), e));
	}

	public static RestException getRestException(ConstraintViolationException e) {
		String messages = e.getConstraintViolations().stream()
				.map(c -> Optional.ofNullable(c.getMessage()).orElse(c.getPropertyPath().toString()))
				.collect(Collectors.joining(", "));
		return getRestException(new RestException(RestStatus.VALID.CODE, messages, e));
	}

	public static RestException getRestException(HttpMessageNotReadableException e) {
		return getRestException(new RestException(RestStatus.VALID.CODE, e.getMessage(), e));
	}
	
	public static RestException getRestException(MissingServletRequestPartException e) {
		return getRestException(new RestException(RestStatus.UNKNOWN.CODE, e.getMessage(), e));
	}
	
	public static RestException getRestException(MaxUploadSizeExceededException e) {
		return getRestException(new RestException(RestStatus.EXCEED_MAX_FILE_SIZE, e));
	}
	
	public static RestException getRestException(ValidationException e) {
		return getRestException(new RestException(RestStatus.VALID.CODE, e.getMessage(), e));
	}
}
