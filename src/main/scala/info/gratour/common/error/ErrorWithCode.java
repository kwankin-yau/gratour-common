package info.gratour.common.error;

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

    public static ErrorWithCode OK = new ErrorWithCode(Errors.OK);
    public static ErrorWithCode INTERNAL_ERROR = new ErrorWithCode(Errors.INTERNAL_ERROR);
    public static ErrorWithCode AUTHENTICATION_FAILED = new ErrorWithCode(Errors.AUTHENTICATION_FAILED);
    public static ErrorWithCode NOT_AUTHENTICATED = new ErrorWithCode(Errors.NOT_AUTHENTICATED);

    public static ErrorWithCode invalidParam(String paramName) {
        return new ErrorWithCode(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName));
    }

    public static ErrorWithCode invalidValue(String fieldName) {
        return new ErrorWithCode(Errors.INVALID_VALUE, Errors.errorMessageFormat(Errors.INVALID_VALUE, fieldName));
    }
    public static ErrorWithCode internalError(String message) {
        message = Errors.errorMessage(Errors.INTERNAL_ERROR) + " " + message;
        return new ErrorWithCode(Errors.INTERNAL_ERROR, message);
    }

}