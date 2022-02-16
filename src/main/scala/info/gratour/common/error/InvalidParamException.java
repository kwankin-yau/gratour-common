/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.error;

public class InvalidParamException extends ErrorWithCode {

    public InvalidParamException(String paramName) {
        super(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_PARAM_FMT, paramName));
    }

    public InvalidParamException(String paramName, Throwable cause) {
        super(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.MESSAGE_KEY_INVALID_PARAM_FMT, paramName), cause);
    }
}
