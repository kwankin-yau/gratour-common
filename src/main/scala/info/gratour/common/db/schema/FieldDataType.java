package info.gratour.common.db.schema;

import info.gratour.common.error.ErrorWithCode;

public enum FieldDataType {

    BOOL, SMALL_INT, INT, BIGINT, TEXT, DECIMAL, FLOAT, DOUBLE, LOCAL_DATE, LOCAL_DATETIME, OFFSET_DATETIME, BINARY;

    public boolean isDateOrTimestamp() {
        return this == LOCAL_DATE || this == LOCAL_DATETIME || this == OFFSET_DATETIME;
    }
    public boolean isText() {
        return this == TEXT;
    }
    public boolean isBool() {
        return this == BOOL;
    }
    public boolean isNumber() {
        switch (this) {
            case SMALL_INT:
            case INT:
            case BIGINT:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
                return true;
            default:
                return false;
        }
    }

    public boolean supportPredication(Predication predication) {
        switch (this) {
            case BOOL:
                return predication == Predication.EQUAL;

            case SMALL_INT:
            case INT:
            case BIGINT:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
            case LOCAL_DATE:
            case LOCAL_DATETIME:
            case OFFSET_DATETIME:
                switch (predication) {
                    case EQUAL:
                    case LESS:
                    case LESS_EQUAL:
                    case GREAT:
                    case GREAT_EQUAL:
                        return true;

                    default:
                        return false;
                }

            case TEXT:
                return predication == Predication.EQUAL || Predication.isLike(predication);

            default:
                throw ErrorWithCode.internalError(String.format("Unhandled case `%s`.", predication.name()));
        }
    }
}
