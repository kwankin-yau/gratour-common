package info.gratour.common.lang;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;

public class InvalidParamException extends ErrorWithCode {

    public InvalidParamException(String paramName) {
        super(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_PARAM_FMT, paramName));
    }

    public InvalidParamException(String paramName, Throwable cause) {
        super(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_PARAM_FMT, paramName), cause);
    }
}
