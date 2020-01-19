package info.gratour.common.db.schema;

import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.error.Errors;

public interface ToDbFieldNameMapper {

    /**
     * Map a field name to one or more database column name.
     * When map to multiple database column name:
     * <ol>
     *     <li>For TableSchema, only the first column name used.</li>
     *     <li>For RowMapper, only the first column name used.</li>
     *     <li>For QueryParamsSpec, the returned database column names are in an `OR` relation.</li>     *
     * </ol>
     *
     * @param apiFieldName
     * @return
     */
    String[] toDbColumnNames(String apiFieldName);

    default String toFirstDbColumnName(String apiFieldName) {
        return checkedToDbColumnNames(apiFieldName)[0];
    }

    default String[] checkedToDbColumnNames(String apiFieldName) {
        String[] r = toDbColumnNames(apiFieldName);
        if (r == null || r.length == 0)
            throw new ErrorWithCode(Errors.INTERNAL_ERROR, "toDbColumnNames returns null or empty.");

        return r;
    }

}
