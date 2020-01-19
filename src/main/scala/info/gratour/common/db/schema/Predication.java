package info.gratour.common.db.schema;

public enum Predication {

    EQUAL, LESS, LESS_EQUAL, GREAT, GREAT_EQUAL, START_WITH, INCLUDE, END_WITH;

    public static boolean isLike(Predication predication) {
        if (predication == null)
            return false;

        switch (predication) {
            case START_WITH:
            case INCLUDE:
            case END_WITH:
                return true;

            default:
                return false;
        }
    }


}
