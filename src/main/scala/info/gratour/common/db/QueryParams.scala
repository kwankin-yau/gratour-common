package info.gratour.common.db

import info.gratour.common.lang.InvalidParamException

case class Pagination(
                       limit: Int,
                       page: Int // 1 based
                     )

case class SortColumn(columnName: String, ascending: Boolean)

case class QueryParams(
                      filter: String,
                      pagination: Pagination,
                      sorting: Array[SortColumn]
                      ) {
}

case class SortableColumn(publicColName: String, internalColName: String)

object SortableColumn {

  private def toInternalColName(publicColName: String): String = {
    val r = new StringBuilder()

    for (c <- publicColName) {
      if (c >= 'A' && c <= 'Z') {
        r.append('_').append(c.toLower)
      } else
        r.append(c)
    }

    r.toString
  }

  def apply(publicColName: String): SortableColumn =
    SortableColumn(publicColName, toInternalColName(publicColName))
}



abstract class QueryParamsSupport(val params: QueryParams) {
  type ColumnNameMapper = PartialFunction[String, String]

  def filterColumns: Array[String]

  if (params.pagination == null) {
    throw new InvalidParamException("pagination")
  }
}

abstract class QueryParamsSortSupport(override val params: QueryParams) extends QueryParamsSupport(params) {

  def sortableColumns: Array[SortableColumn]

  if (params.sorting != null) {
    val sortable = sortableColumns

    if (params.sorting.length > 0 && sortable.isEmpty) {
      throw new InvalidParamException("sorting")
    }

    for (i <- params.sorting.indices) {
      val col = params.sorting(i)

      if (!sortable.exists(sc => sc.publicColName == col.columnName))
        throw new InvalidParamException(s"sorting: `${col.columnName}`")
    }

  }
}

abstract class QueryParamsSortSupportDefaultMapping(override val params: QueryParams) extends QueryParamsSortSupport(params) {
  def sortableColumnPublicNames: Array[String]

  def sortableColumns: Array[SortableColumn] =
    sortableColumnPublicNames.map(name => SortableColumn(name))

}
