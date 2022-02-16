/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.error;

import java.util.Locale;
import java.util.ResourceBundle;

public class Errors {

    public static final int OK = 0;
    public static final String MESSAGE_KEY_OK = "errors.ok";
    public static final String OK_MESSAGE = "OK.";

    public static final int UNKNOWN_ERROR = -1;
    public static final String MESSAGE_KEY_UNKNOWN_ERROR = "errors.unknown";
    public static final String MESSAGE_KEY_UNKNOWN_ERROR_CODE_FMT = "errors.unknown_errcode_fmt";
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error.";

    public static final int INTERNAL_ERROR = -2;
    public static final String MESSAGE_KEY_INTERNAL_ERROR = "errors.internal";
    public static final String INTERNAL_ERROR_MESSAGE = "Internal error.";

    public static final int EXECUTION_ERROR = -3;
    public static final String MESSAGE_KEY_EXECUTION_ERROR = "errors.execution";
    public static final String EXECUTION_ERROR_MESSAGE = "Execution error.";

    public static final int INVALID_CONFIG = -4;
    public static final String MESSAGE_KEY_INVALID_CONFIG = "errors.invalid_config";
    public static final String INVALID_CONFIG_MESSAGE = "Invalid configuration.";

    public static final int TIMEOUT = -5;
    public static final String MESSAGE_KEY_TIMEOUT = "errors.timeout";
    public static final String TIMEOUT_MESSAGE = "Timeout error.";

    public static final int ILLEGAL_STATE = -6;
    public static final String MESSAGE_KEY_ILLEGAL_STATE = "errors.illegal_state";
    public static final String ILLEGAL_STATE_MESSAGE = "Illegal state.";

    public static final int INVALID_PARAM = -7;
    public static final String MESSAGE_KEY_INVALID_PARAM = "errors.invalid_param";
    public static final String MESSAGE_KEY_INVALID_PARAM_FMT = "errors.invalid_param_fmt";
    public static final String INVALID_PARAM_MESSAGE = "Invalid parameter.";

    public static final int RECORD_NOT_FOUND = -8;
    public static final String MESSAGE_KEY_RECORD_NOT_FOUND = "errors.record_not_found";
    public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found.";

    public static final int AUTHENTICATION_FAILED = -9;
    public static final String MESSAGE_KEY_AUTHENTICATION_FAILED = "errors.auth_failed";

    public static final int INVALID_TOKEN = -10;
    public static final String MESSAGE_KEY_INVALID_TOKEN = "errors.invalid_token";

    public static final int ACCESS_DENIED = -11;
    public static final String MESSAGE_KEY_ACCESS_DENIED = "errors.access_denied";

    public static final int SESSION_EXPIRED = -12;
    public static final String MESSAGE_KEY_SESSION_EXPIRED = "errors.session_expired";

    public static final int BAD_REQUEST = -13;
    public static final String MESSAGE_KEY_BAD_REQUEST = "errors.bad_request";

    public static final int UNSUPPORTED_TYPE = -14;
    public static final String MESSAGE_KEY_UNSUPPORTED_TYPE = "errors.unsupported_type";

    public static final int VCODE_EXPIRED = -15;
    public static final String MESSAGE_KEY_VCODE_EXPIRED = "errors.vcode_expired";

    public static final int INVALID_VCODE = -16;
    public static final String MESSAGE_KEY_INVALID_VCODE = "errors.invalid_vcode";

    public static final int DATA_MISMATCH = -17;
    public static final String MESSAGE_KEY_DATA_MISMATCH_FMT = "errors.data_mismatch_fmt";

    public static final int NOT_AUTHENTICATED = -18;
    public static final String MESSAGE_KEY_NOT_AUTHENTICATED = "errors.not_authenticated";

    public static final int NOT_ENOUGH_PRIV = -19;
    public static final String MESSAGE_KEY_NOT_ENOUGH_PRIV = "errors.not_enough_priv";

    public static final int USER_INFO_NOT_ENOUGH = -20;
    public static final String MESSAGE_KEY_USER_INFO_NOT_ENOUGH = "errors.user_info_not_enough";

    public static final int RESOURCE_OCCUPIED = -21;
    public static final String MESSAGE_KEY_RESOURCE_OCCUPIED = "errors.resource_occupied";

    public static final int NOT_SUPPORTED = -22;
    public static final String MESSAGE_KEY_NOT_SUPPORTED = "errors.not_supported";

    public static final int TOO_MANY_REQUEST = -23;
    public static final String MESSAGE_KEY_TOO_MANY_REQUEST = "errors.too_many_request";

    public static final int IO_ERROR = -24;
    public static final String MESSAGE_KEY_IO_ERROR = "errors.io_error";

    public static final int NO_CTRL = -25;
    public static final String MESSAGE_KEY_NO_CTRL = "errors.no_ctrl";

    public static final int RULE_VIOLATED = -26;
    public static final String MESSAGE_KEY_RULE_VIOLATED = "errors.rule_violated";

    public static final int NO_CONNECTION_AVAILABLE = -27;
    public static final String MESSAGE_KEY_NO_CONNECTION_AVAILABLE = "errors.no_connection_available";

    public static final int CANCELED = -28;
    public static final String MESSAGE_KEY_CANCELED = "errors.canceled";

    public static final int CODEC_ERROR = -29;
    public static final String MESSAGE_KEY_CODEC_ERROR = "errors.codec_error";
    public static final String MESSAGE_KEY_CODEC_ERROR_FMT = "errors.codec_error_fmt";

    // service errors
    public static final int SERVICE_ALREADY_STARTED = -100;
    public static final String MESSAGE_KEY_SERVICE_ALREADY_STARTED = "errors.serv_already_started";
    public static final String SERVICE_ALREADY_STARTED_MESSAGE = "Service already started.";

    public static final int SERVICE_IS_STARTING = -101;
    public static final String MESSAGE_KEY_SERVICE_IS_STARTING = "errors.serv_is_starting";
    public static final String SERVICE_IS_STARTING_MESSAGE = "Service is starting.";

    public static final int SERVICE_IS_STOPPING = -102;
    public static final String MESSAGE_KEY_SERVICE_IS_STOPPING = "errors.serv_is_stopping";
    public static final String SERVICE_IS_STOPPING_MESSAGE = "Service is stopping.";

    public static final int SERVICE_IS_STOPPED = -103;
    public static final String MESSAGE_KEY_SERVICE_IS_STOPPED = "errors.serv_is_stopped";
    public static final String SERVICE_IS_STOPPED_MESSAGE = "Service is stopped.";

    public static final int HTTP_METHOD_NOT_SUPPORT = -104;
    public static final String MESSAGE_KEY_HTTP_METHOD_NOT_SUPPORT = "errors.http_method_not_support";

    public static final int MISSING_REQUEST_PARAM = -105;
    public static final String MESSAGE_KEY_MISSING_REQUEST_PARAM = "errors.missing_request_param";

    public static final int SERVICE_UNAVAILABLE = -106;
    public static final String MESSAGE_KEY_SERVICE_UNAVAILABLE = "errors.service_not_available";


    /**
     * @deprecated Use {@link #SERVICE_UNAVAILABLE} instead
     */
    @Deprecated
    public static final int SERVICE_NOT_AVAILABLE = SERVICE_UNAVAILABLE;

    /**
     * @deprecated Use {@link #MESSAGE_KEY_SERVICE_UNAVAILABLE} instead
     */
    @Deprecated
    public static final String MESSAGE_KEY_SERVICE_NOT_AVAILABLE = MESSAGE_KEY_SERVICE_UNAVAILABLE;

    public static final int SERVICE_BUSY = -107;
    public static final String MESSAGE_KEY_SERVICE_BUSY = "errors.service_busy";

    // script errors
    public static final int BAD_FORMAT = -200;
    public static final String MESSAGE_KEY_BAD_FORMAT = "errors.bad_format";
    public static final String BAD_FORMAT_MESSAGE = "Bad format.";

    public static final int FILE_NOT_FOUND = -201;
    public static final String MESSAGE_KEY_FILE_NOT_FOUND = "errors.file_not_found";
    public static final String FILE_NOT_FOUND_MESSAGE = "File not found.";

    public static final int SYNTAX_ERROR = -202;
    public static final String MESSAGE_KEY_SYNTAX_ERROR = "errors.syntax_error";
    public static final String SYNTAX_ERROR_MESSAGE = "Syntax error.";

    public static final int EOF = -203;
    public static final String MESSAGE_KEY_EOF = "errors.eof";
    public static final String EOF_MESSAGE = "End of file.";

    public static final int DUPLICATED_VALUE = -204;
    public static final String MESSAGE_KEY_DUPLICATED_VALUE = "errors.duplicated_value";

    public static final int INVALID_VALUE = -205;
    public static final String MESSAGE_KEY_INVALID_VALUE = "errors.invalid_value";
    public static final String MESSAGE_KEY_INVALID_VALUE_FMT = "errors.invalid_value_fmt";

    public static final int FOREIGN_KEY_VIOLATION = -206;
    public static final String MESSAGE_KEY_FOREIGN_KEY_VIOLATION = "errors.foreign_key_violation";

    public static final int RETRY = -207;
    public static final String MESSAGE_KEY_RETRY = "errors.retry";

    public static boolean isOk(int errorCode) {
        return errorCode == OK;
    }

    public static boolean isError(int errorCode) {
        return errorCode != OK;
    }

    public static String messageKey(int errorCode) {
        switch (errorCode) {
            case OK:
                return MESSAGE_KEY_OK;

            case UNKNOWN_ERROR:
                return MESSAGE_KEY_UNKNOWN_ERROR;


            case INTERNAL_ERROR:
                return MESSAGE_KEY_INTERNAL_ERROR;


            case EXECUTION_ERROR:
                return MESSAGE_KEY_EXECUTION_ERROR;


            case INVALID_CONFIG:
                return MESSAGE_KEY_INVALID_CONFIG;

            case TIMEOUT:
                return MESSAGE_KEY_TIMEOUT;


            case ILLEGAL_STATE:
                return MESSAGE_KEY_ILLEGAL_STATE;


            case INVALID_PARAM:
                return MESSAGE_KEY_INVALID_PARAM;


            case RECORD_NOT_FOUND:
                return MESSAGE_KEY_RECORD_NOT_FOUND;

            case AUTHENTICATION_FAILED:
                return MESSAGE_KEY_AUTHENTICATION_FAILED;

            case INVALID_TOKEN:
                return MESSAGE_KEY_INVALID_TOKEN;

            case ACCESS_DENIED:
                return MESSAGE_KEY_ACCESS_DENIED;

            case SESSION_EXPIRED:
                return MESSAGE_KEY_SESSION_EXPIRED;

            case BAD_REQUEST:
                return MESSAGE_KEY_BAD_REQUEST;

            case UNSUPPORTED_TYPE:
                return MESSAGE_KEY_UNSUPPORTED_TYPE;

            case VCODE_EXPIRED:
                return MESSAGE_KEY_VCODE_EXPIRED;

            case INVALID_VCODE:
                return MESSAGE_KEY_INVALID_VCODE;

            case DATA_MISMATCH:
                return MESSAGE_KEY_DATA_MISMATCH_FMT;

            case NOT_AUTHENTICATED:
                return MESSAGE_KEY_NOT_AUTHENTICATED;

            case NOT_ENOUGH_PRIV:
                return MESSAGE_KEY_NOT_ENOUGH_PRIV;

            case USER_INFO_NOT_ENOUGH:
                return MESSAGE_KEY_USER_INFO_NOT_ENOUGH;

            case RESOURCE_OCCUPIED:
                return MESSAGE_KEY_RESOURCE_OCCUPIED;

            case NOT_SUPPORTED:
                return MESSAGE_KEY_NOT_SUPPORTED;

            case TOO_MANY_REQUEST:
                return MESSAGE_KEY_TOO_MANY_REQUEST;

            case IO_ERROR:
                return MESSAGE_KEY_IO_ERROR;

            case NO_CTRL:
                return MESSAGE_KEY_NO_CTRL;

            case RULE_VIOLATED:
                return MESSAGE_KEY_RULE_VIOLATED;

            case CANCELED:
                return MESSAGE_KEY_CANCELED;

            case CODEC_ERROR:
                return MESSAGE_KEY_CODEC_ERROR;

            // service error
            case SERVICE_ALREADY_STARTED:
                return MESSAGE_KEY_SERVICE_ALREADY_STARTED;


            case SERVICE_IS_STARTING:
                return MESSAGE_KEY_SERVICE_IS_STARTING;


            case SERVICE_IS_STOPPING:
                return MESSAGE_KEY_SERVICE_IS_STOPPING;


            case SERVICE_IS_STOPPED:
                return MESSAGE_KEY_SERVICE_IS_STOPPED;

            case HTTP_METHOD_NOT_SUPPORT:
                return MESSAGE_KEY_HTTP_METHOD_NOT_SUPPORT;

            case MISSING_REQUEST_PARAM:
                return MESSAGE_KEY_MISSING_REQUEST_PARAM;

            case SERVICE_UNAVAILABLE:
                return MESSAGE_KEY_SERVICE_UNAVAILABLE;

            case SERVICE_BUSY:
                return MESSAGE_KEY_SERVICE_BUSY;

            case NO_CONNECTION_AVAILABLE:
                return MESSAGE_KEY_NO_CONNECTION_AVAILABLE;


            // script errors
            case BAD_FORMAT:
                return MESSAGE_KEY_BAD_FORMAT;


            case FILE_NOT_FOUND:
                return MESSAGE_KEY_FILE_NOT_FOUND;


            case SYNTAX_ERROR:
                return MESSAGE_KEY_SYNTAX_ERROR;


            case EOF:
                return MESSAGE_KEY_EOF;

            case DUPLICATED_VALUE:
                return MESSAGE_KEY_DUPLICATED_VALUE;

            case INVALID_VALUE:
                return MESSAGE_KEY_INVALID_VALUE;

            case FOREIGN_KEY_VIOLATION:
                return MESSAGE_KEY_FOREIGN_KEY_VIOLATION;

            case RETRY:
                return MESSAGE_KEY_RETRY;

            default:
                return null;
        }
    }

    public static String messageFormatKey(int errorCode) {
        switch (errorCode) {
            case INVALID_PARAM:
                return MESSAGE_KEY_INVALID_PARAM_FMT;


            case DATA_MISMATCH:
                return MESSAGE_KEY_DATA_MISMATCH_FMT;

            case INVALID_VALUE:
                return MESSAGE_KEY_INVALID_VALUE_FMT;

            case CODEC_ERROR:
                return MESSAGE_KEY_CODEC_ERROR_FMT;

            default:
                return messageKey(errorCode);
        }
    }

    private static ResourceBundle resourceBundle(Locale locale) {
        return ResourceBundle.getBundle("info.gratour.common.messages", locale != null ? locale : Locale.getDefault());
    }

    public static String errorMessage(int errorCode, Locale locale) {
        String key = messageKey(errorCode);
        if (key == null)
            return errorMessageFormat(MESSAGE_KEY_UNKNOWN_ERROR_CODE_FMT, String.valueOf(errorCode), locale);

        return resourceBundle(locale).getString(key);
    }

    public static String errorMessage(int errorCode) {
        return errorMessage(errorCode, Locale.getDefault());
    }


    public static String errorMessageFormat(String messageKey, String arg, Locale locale) {
        if (messageKey == null)
            return "Unknown error.";

        return String.format(resourceBundle(locale).getString(messageKey), arg);
    }

    public static String errorMessageFormat(int errorCode, String arg, Locale locale) {
        String messageKey = messageFormatKey(errorCode);
        if (messageKey == null)
            return "Unknown error.";

        return String.format(resourceBundle(locale).getString(messageKey), arg);
    }


    public static String errorMessageFormat(String messageKey, String arg) {
        return errorMessageFormat(messageKey, arg, Locale.getDefault());
    }

    public static String errorMessageFormat(int errorCode, String arg) {
        return errorMessageFormat(errorCode, arg, Locale.getDefault());
    }

}
