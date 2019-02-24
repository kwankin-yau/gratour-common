package info.gratour.common.lang;

import info.gratour.common.Errors;

public class ErrorWithCode extends RuntimeException {

    private int errCode;

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
}
