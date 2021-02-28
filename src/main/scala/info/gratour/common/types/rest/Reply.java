package info.gratour.common.types.rest;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;
import info.gratour.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Reply<T> extends RawReply {

//    public static final Type TYPE = new TypeToken<Reply>() {
//    }.getType();

    public static interface Mapper<T, C> {

        C map(T entry);
    }


    private T[] data;
    private Long count;


    public Reply() {
    }

    public Reply(int errCode) {
        this(errCode, Errors.errorMessage(errCode));
    }

    public Reply(int errCode, String message) {
        super(errCode, message);
    }

    public Reply(T[] data, Long count) {
        this.data = data;
        this.count = count;
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

//    public boolean isOk() {
//        return errCode == Errors.OK;
//    }

    @Override
    public boolean hasData() {
        return data != null && data.length > 0;
    }

    /**
     *
     * @return a non-empty list
     */
    public List<T> dataList() {
        if (data != null)
            return Arrays.asList(data);
        else
            return new ArrayList<>();
    }

    public T first() {
        if (!hasData())
            throw new ErrorWithCode(Errors.INTERNAL_ERROR, "Reply has no data.");
        return data[0];
    }

    public T firstOrNull() {
        if (hasData())
            return data[0];
        else
            return null;
    }

    @SuppressWarnings({"unchecked"})
    public <C> Reply<C> map(Mapper<T, C> mapper) {
        if (hasData()) {
            C[] arr = (C[]) new Object[data.length];
            for (int i = 0; i < data.length; i++) {
                C c = mapper.map(data[i]);
                arr[i] = c;
            }

            return new Reply<C>(arr, count);
        } else
            return new Reply<>((C[]) new Object[0], count);
    }

    public void forEach(Consumer<T> consumer) {
        if (hasData() && consumer != null) {
            for (T datum : data)
                consumer.accept(datum);
        }
    }

    public ErrorWithCode toErrorWithCode() {
        return new ErrorWithCode(errCode, message);
    }

    public static Reply<?> ofUpdateResult(boolean recordUpdated) {
        if (recordUpdated)
            return OK;
        else
            return RECORD_NOT_FOUND;
    }

    public static Reply<?> ofUpdateResult(int updateRecordCount) {
        return ofUpdateResult(updateRecordCount > 0);
    }

    public static <T> Reply<T> single(T data) {
        if (data == null)
            return new Reply<>(Errors.RECORD_NOT_FOUND);

        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[]{data};
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

    public static <T> Reply<T> multi(T[] data) {
        return multi(data, data.length);
    }

    public static <T> Reply<T> multi(List<T> data, long totalRecordCount) {
        Reply<T> r = new Reply<T>(Errors.OK);
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) new Object[data.size()];
        data.toArray(arr);

        r.setData(arr);
        r.setCount(totalRecordCount);

        return r;
    }

    public static <T> Reply<T> multi(List<T> data) {
        return multi(data, data.size());
    }

    public static <T> Reply<T> error(int errCode) {
        return error(errCode, Errors.errorMessage(errCode));
    }

    public static <T> Reply<T> error(int errCode, String message) {
        return new Reply<T>(errCode, message);
    }

    public static <T> Reply<T> error(ErrorWithCode e) {
        return error(e.getErrCode(), e.getMessage());
    }

    @Override
    public String toString() {
        return "Resp{" +
                "errCode=" + errCode +
                ", message='" + message + '\'' +
                ", data=" + StringUtils.arrayToString(data) +
                ", count=" + count +
                '}';
    }

    @SuppressWarnings({"rawtypes"})
    public static final Reply OK = new Reply<>(Errors.OK);
    @SuppressWarnings({"rawtypes"})
    public static final Reply INTERNAL_ERROR = new Reply<>(Errors.INTERNAL_ERROR);
    @SuppressWarnings({"rawtypes"})
    public static final Reply AUTHENTICATION_FAILED = new Reply<>(Errors.AUTHENTICATION_FAILED);
    @SuppressWarnings({"rawtypes"})
    public static final Reply BAD_FORMAT = new Reply<>(Errors.BAD_FORMAT);
    @SuppressWarnings({"rawtypes"})
    public static final Reply DUPLICATED_VALUE = new Reply<>(Errors.DUPLICATED_VALUE);
    @SuppressWarnings({"rawtypes"})
    public static final Reply RECORD_NOT_FOUND = new Reply<>(Errors.RECORD_NOT_FOUND);
    @SuppressWarnings({"rawtypes"})
    public static final Reply ACCESS_DENIED = new Reply<>(Errors.ACCESS_DENIED);
    @SuppressWarnings({"rawtypes"})
    public static final Reply SESSION_EXPIRED = new Reply<>(Errors.SESSION_EXPIRED);
    @SuppressWarnings({"rawtypes"})
    public static final Reply BAD_REQUEST = new Reply<>(Errors.BAD_REQUEST);
}
