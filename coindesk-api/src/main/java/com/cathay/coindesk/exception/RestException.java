package com.cathay.coindesk.exception;

import com.cathay.coindesk.rest.RestStatus;
import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class RestException  extends RuntimeException  {

	private String errorCode = null;
	private String msg = null;
	private Object errorData = null;
	
	/**
	 * Constructor
	 */
	public RestException() {
		super();
	}
	
	public RestException(RestStatus status, String msg, Throwable cause) {
		super(msg, cause);
		this.errorCode = status.CODE;
	}
	
	public RestException(RestStatus status, Throwable cause) {
		super(status.MESSAGE, cause);
		this.errorCode = status.CODE;
	}
	
	public RestException(String code, String msg, Throwable cause) {
		super(msg, cause);
		this.errorCode = code;
	}
	
	public RestException(String code, String msg, Object errorData, Throwable cause) {
		super(msg, cause);
		this.errorCode = code;
		this.errorData = errorData;
	}
}
