/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.error;

public class EAbort extends RuntimeException {
    public EAbort() {
    }

    public EAbort(String message) {
        super(message);
    }

    public EAbort(String message, Throwable cause) {
        super(message, cause);
    }

    public EAbort(Throwable cause) {
        super(cause);
    }

    public EAbort(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
