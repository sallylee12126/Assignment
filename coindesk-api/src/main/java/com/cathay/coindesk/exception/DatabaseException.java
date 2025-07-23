package com.cathay.coindesk.exception;

@SuppressWarnings("serial")
public class DatabaseException extends Exception {
	/**
	 * Constructor
	 */
	public DatabaseException() {
		super();
	}
	
	
	
	public DatabaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor
	 * 
	 * @param sMsg
	 * @param cause
	 */
	public DatabaseException(String sMsg, Throwable cause) {
		super(sMsg, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param sMsg
	 */
	public DatabaseException(String sMsg) {
		super(sMsg);
	}

	public String toString() {
		
		Throwable th = getCause();
		StringBuffer sb = new StringBuffer(super.toString());
		if (th != null) {
			sb.append(", caused = " + th);
		}
		
		return sb.toString();
	}
}
