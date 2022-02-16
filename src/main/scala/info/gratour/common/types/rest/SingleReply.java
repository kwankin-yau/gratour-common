/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types.rest;

public class SingleReply<T> extends RawReply {

    public SingleReply() {
    }

    public SingleReply(int errCode) {
        super(errCode);
    }

    public SingleReply(int errCode, String message) {
        super(errCode, message);
    }

    public SingleReply(T data) {
        this.data = data;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean hasData() {
        return data != null;
    }

    @Override
    public String toString() {
        return "SingleReply{" +
                "data=" + data +
                "} " + super.toString();
    }
}
