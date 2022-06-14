/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.error;

public class ErrorWithCode extends RuntimeException {

    private final int errCode;

    public ErrorWithCode(int errCode) {
        this(errCode, Errors.errorMessage(errCode), null);
    }

    public ErrorWithCode(int errCode, String message) {
        this(errCode, message, null);

    }

    public ErrorWithCode(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public ErrorWithCode(int errCode, Throwable cause) {
        super(Errors.errorMessage(errCode), cause);
        this.errCode = errCode;
    }

    public ErrorWithCode(int errCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public boolean ok() {
        return errCode == Errors.OK;
    }

    public static ErrorWithCode OK = new ErrorWithCode(Errors.OK);
    public static ErrorWithCode INTERNAL_ERROR = new ErrorWithCode(Errors.INTERNAL_ERROR);
    public static ErrorWithCode AUTHENTICATION_FAILED = new ErrorWithCode(Errors.AUTHENTICATION_FAILED);
    public static ErrorWithCode NOT_AUTHENTICATED = new ErrorWithCode(Errors.NOT_AUTHENTICATED);
    public static ErrorWithCode ACCESS_DENIED = new ErrorWithCode(Errors.ACCESS_DENIED);
    public static ErrorWithCode NOT_ENOUGH_PRIV = new ErrorWithCode(Errors.NOT_ENOUGH_PRIV);
    public static ErrorWithCode RECORD_NOT_FOUND = new ErrorWithCode(Errors.RECORD_NOT_FOUND);
    public static ErrorWithCode EXECUTION_ERROR = new ErrorWithCode(Errors.EXECUTION_ERROR);
    public static ErrorWithCode ILLEGAL_STATE = new ErrorWithCode(Errors.ILLEGAL_STATE);
    public static ErrorWithCode INVALID_CONFIG = new ErrorWithCode(Errors.INVALID_CONFIG);
    public static ErrorWithCode TIMEOUT = new ErrorWithCode(Errors.TIMEOUT);
    public static ErrorWithCode SERVICE_UNAVAILABLE = new ErrorWithCode(Errors.SERVICE_UNAVAILABLE);

    public static ErrorWithCode invalidParam(String paramName) {
        return new ErrorWithCode(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName));
    }

    public static ErrorWithCode invalidParam(String paramName, String reason) {
        String message = Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName) + " " + reason;
        return new ErrorWithCode(Errors.INVALID_PARAM, message);
    }

    public static ErrorWithCode invalidValue(String fieldName) {
        return new ErrorWithCode(Errors.INVALID_VALUE, Errors.errorMessageFormat(Errors.INVALID_VALUE, fieldName));
    }

    public static ErrorWithCode internalError(String message) {
        message = Errors.errorMessage(Errors.INTERNAL_ERROR) + " " + message;
        return new ErrorWithCode(Errors.INTERNAL_ERROR, message);
    }

    public static ErrorWithCode format(int errCode, String formatArg) {
        String message = Errors.errorMessageFormat(errCode, formatArg);
        return new ErrorWithCode(errCode, message);
    }

}
