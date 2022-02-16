/*******************************************************************************
 *  Copyright (c) 2019, 2020 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.types.rest;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;

public class RawReply {

    protected int errCode;
    protected String message;

    public RawReply() {
    }

    public RawReply(int errCode) {
        this(errCode, Errors.errorMessage(errCode));
    }

    public RawReply(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean ok() {
        return errCode == Errors.OK;
    }

    public boolean hasData() {
        return false;
    }

    public ErrorWithCode toErrorWithCode() {
        return new ErrorWithCode(errCode, message);
    }

    public static RawReply ofUpdateResult(boolean recordUpdated) {
        if (recordUpdated)
            return OK;
        else
            return RECORD_NOT_FOUND;
    }

    public static RawReply ofUpdateResult(int updateRecordCount) {
        return ofUpdateResult(updateRecordCount > 0);
    }

    @Override
    public String toString() {
        return "RawReply{" +
                "errCode=" + errCode +
                ", message='" + message + '\'' +
                '}';
    }

    public static RawReply err(int errCode) {
        return new RawReply(errCode);
    }

    public static RawReply err(int errCode, String message) {
        return new RawReply(errCode, message);
    }

    public static RawReply invalidParamRaw(String paramName) {
        return new RawReply(Errors.INVALID_PARAM, Errors.errorMessageFormat(Errors.INVALID_PARAM, paramName));
    }

    public static final RawReply OK = new RawReply(Errors.OK);
    public static final RawReply INTERNAL_ERROR = new RawReply(Errors.INTERNAL_ERROR);
    public static final RawReply AUTHENTICATION_FAILED = new RawReply(Errors.AUTHENTICATION_FAILED);
    public static final RawReply BAD_FORMAT = new RawReply(Errors.BAD_FORMAT);
    public static final RawReply DUPLICATED_VALUE = new RawReply(Errors.DUPLICATED_VALUE);
    public static final RawReply RECORD_NOT_FOUND = new RawReply(Errors.RECORD_NOT_FOUND);
    public static final RawReply ACCESS_DENIED = new RawReply(Errors.ACCESS_DENIED);
    public static final RawReply SESSION_EXPIRED = new RawReply(Errors.SESSION_EXPIRED);
    public static final RawReply BAD_REQUEST = new RawReply(Errors.BAD_REQUEST);
    public static final RawReply HTTP_METHOD_NOT_SUPPORT = new RawReply(Errors.HTTP_METHOD_NOT_SUPPORT);
    public static final RawReply MISSING_REQUEST_PARAM = new RawReply(Errors.MISSING_REQUEST_PARAM);
    public static final RawReply SERVICE_UNAVAILABLE = new RawReply(Errors.SERVICE_UNAVAILABLE);
    public static final RawReply TIMEOUT = new RawReply(Errors.TIMEOUT);
    public static final RawReply EXECUTION_ERROR = new RawReply(Errors.EXECUTION_ERROR);
}
