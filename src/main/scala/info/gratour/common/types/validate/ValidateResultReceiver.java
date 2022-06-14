/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.types.validate;

public interface ValidateResultReceiver {
    boolean ok();
    default boolean error() {
        return !ok();
    }

    void invalidField(String fieldName);
    void error(String message);
}
