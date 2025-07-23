package com.cathay.coindesk.exception;

import com.cathay.coindesk.error.ErrorStatus;
import com.cathay.coindesk.error.IErrorCode;
import com.cathay.coindesk.error.SeverityType;
import lombok.Getter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>
 * 異常物件
 */
@Getter
@SuppressWarnings("serial")
public class ActionException extends Exception {

    /** 回應狀態 */
    protected ErrorStatus status = null;

    /** chain to next exception */
    protected ActionException next = null;

    protected Object data = null;

    public ActionException(String msg, ErrorStatus status, Throwable cause) {
        super(msg, cause);

        this.status = status;
        this.status.setErrorDesc(msg);
    }

    public ActionException(String msg, ErrorStatus status, Throwable cause, Object data) {
        super(msg, cause);

        this.status = status;
        this.status.setErrorDesc(msg);
        this.data = data;
    }

    public ActionException(ErrorStatus error, Throwable cause) {
        this(error.getErrorDesc(), error, cause);
    }

    public ActionException(ErrorStatus error) {
        this(error.getErrorDesc(), error, null);
    }

    public ActionException(String msg, IErrorCode error, Throwable cause) {
        this(msg, error.getError(), cause);

    }

    public ActionException(IErrorCode error) {
        this(error.getError().getErrorDesc(), error.getError(), null);
    }

    public ActionException(IErrorCode error, Object data) {
        this(error.getError().getErrorDesc(), error.getError(), null);
        this.data = data;
    }

    public ActionException(String msg, IErrorCode error) {
        this(msg, error.getError(), null);
    }

    public ActionException(IErrorCode error, Throwable cause) {
        this(error.getError().getErrorDesc(), error.getError(), cause);
    }

    public ActionException(String systemId, String errorCode, SeverityType severity, String errorDesc) {
        this(errorDesc, new ErrorStatus(systemId, errorCode, severity, errorDesc), null);
    }

    public ActionException(String systemId, String errorCode, SeverityType severity, String errorDesc, Throwable cause) {
        this(errorDesc, new ErrorStatus(systemId, errorCode, severity, errorDesc), cause);
    }

    public ActionException(String systemId, String errorCode, SeverityType severity, String errorDesc, Object data) {
        this(errorDesc, new ErrorStatus(systemId, errorCode, severity, errorDesc), null, data);
    }

    /**
     * 設定 狀態資料
     *
     * @param status
     */
    public void setStatus(ErrorStatus status) {
        this.status = status;
    }

    /**
     * 取得 狀態資料
     *
     * @return
     */
    public ErrorStatus getStatus() {
        return status;
    }

    /**
     * 取得異常系統代碼
     *
     * @return
     */
    public String getSystemId() {
        return status.getSystemId();
    }

    /**
     * 取得錯誤代碼
     *
     * @return
     */
    public String getErrorCode() {
        return status.getErrorCode();
    }

    /**
     * 取得錯誤描述
     *
     * @return
     */
    public String getErrorDesc() {
        return status.getErrorDesc();
    }

    /**
     * 設定Severity
     *
     * @param severity
     */
    public void setSeverity(SeverityType severity) {
        status.setSeverity(severity);
    }

    /**
     * 取得Severity
     *
     * @return
     */
    public SeverityType getSeverity() {
        return status.getSeverity();
    }


    /**
     * Adds an <code>ActionException</code> object to the end of the chain.
     *
     * @param ex the new exception that will be added to the end of the
     *            <code>ActionException</code> chain
     * @see #getNextException
     */
    public synchronized void setNextException(ActionException ex) {
        ActionException theEnd = this;
        while (theEnd.next != null) {
            theEnd = theEnd.next;
        }
        theEnd.next = ex;
    }

    /**
     * Retrieves the exception chained to this <code>ActionException</code>
     * object.
     *
     * @return the next <code>ActionException</code> object in the chain;
     *         <code>null</code> if there are none
     * @see #setNextException
     */
    public ActionException getNextException() {
        return (next);
    }

    /**
     * 是否為TIMEOUT的錯誤
     *
     * @return
     */
    public boolean isTimeout() {

        return status.getSeverity().isTimeout();
    }


    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());

        sb.append(", error system id = ").append(status.getSystemId());
        sb.append(", error code = ").append(status.getErrorCode());
        sb.append(", severity = ").append(status.getSeverity());
        sb.append(", error desc = ").append(status.getErrorDesc());

        sb.append("\n");
        sb.append(getStackTrace(getCause()));


        return sb.toString();
    }

    public static String getStackTrace(Throwable error) {
        final Writer result = new StringWriter();
        if (error != null) {
            final PrintWriter printWriter = new PrintWriter(result);


            error.printStackTrace(printWriter);
        }
        return result.toString();
    }

    public void setErrorCode(String errorCode) {
        status.setErrorCode(errorCode);
    }

}