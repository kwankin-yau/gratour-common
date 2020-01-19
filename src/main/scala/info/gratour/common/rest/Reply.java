package info.gratour.common.rest;

import com.google.gson.reflect.TypeToken;
import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;
import info.gratour.common.utils.StringUtils;


import java.lang.reflect.Type;
import java.util.List;

public class Reply<T> {

    public static final Type TYPE = new TypeToken<Reply>(){}.getType();

    private int errCode;
    private String message;
    private T[] data;
    private Long count;

    public Reply() {
    }

    public Reply(int errCode) {
        this(errCode, Errors.errorMessage(errCode));
    }

    public Reply(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }

    public Reply(T[] data, Long count) {
        this.data = data;
        this.count = count;
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

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isOk() {
        return errCode == Errors.OK;
    }


    public ErrorWithCode toErrorWithCode() {
        return new ErrorWithCode(errCode, message);
    }

    public static <T> Reply<T> single(T data) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[])new Object[]{data};
        Reply<T> r = new Reply<T>(Errors.OK);
        r.setData(arr);

        return r;
    }

    public static <T> Reply<T> multi(T[] data, long totalRecordCount) {
        Reply<T> r = new Reply<T>(Errors.OK);
        r.setData(data);
        r.setCount(totalRecordCount);

        return r;
    }

    public static <T> Reply<T> multi(List<T> data, long totalRecordCount) {
        Reply<T> r = new Reply<T>(Errors.OK);
        @SuppressWarnings("unchecked")
        T[] arr = (T[])new Object[data.size()];
        data.toArray(arr);

        r.setData(arr);
        r.setCount(totalRecordCount);

        return r;
    }

    @Override
    public String toString() {
        return "Resp{" +
                "errCode=" + errCode +
                ", errMsg='" + message + '\'' +
                ", data=" + StringUtils.arrayToString(data) +
                ", count=" + count +
                '}';
    }

    public static final Reply OK = new Reply(Errors.OK);
    public static final Reply INTERNAL_ERROR = new Reply(Errors.INTERNAL_ERROR);
    public static final Reply AUTHENTICATION_FAILED = new Reply(Errors.AUTHENTICATION_FAILED);
    public static final Reply BAD_FORMAT = new Reply(Errors.BAD_FORMAT);
    public static final Reply DUPLICATED_VALUE = new Reply(Errors.DUPLICATED_VALUE);
    public static final Reply RECORD_NOT_FOUND = new Reply(Errors.RECORD_NOT_FOUND);
    public static final Reply ACCESS_DENIED = new Reply(Errors.ACCESS_DENIED);
    public static final Reply SESSION_EXPIRED = new Reply(Errors.SESSION_EXPIRED);
    public static final Reply BAD_REQUEST = new Reply(Errors.BAD_REQUEST);
}
