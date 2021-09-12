/*******************************************************************************
 *  Copyright (c) 2019, 2020 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
package info.gratour.common.types;

public enum UpdateKind {
    INSERT("ins"),
    UPDATE("upd"),
    DELETE("del");

    private final String act;

    UpdateKind(String act) {
        this.act = act;
    }

    public String getAct() {
        return act;
    }

    public boolean isInsert() {
        return this == INSERT;
    }

    public boolean isUpdate() {
        return this == UPDATE;
    }

    public boolean isDelete() {
        return this == DELETE;
    }

    public static UpdateKind ofAct(String act) {
        switch (act) {
            case "ins":
                return INSERT;

            case "upd":
                return UPDATE;

            case "del":
                return DELETE;

            default:
                return null;
        }
    }
}
