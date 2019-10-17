package info.gratour.common.db

class SimpleSelectQuery(sql: String) {

  private val str = new StringBuilder(sql)
  private val whereClause = new StringBuilder()
  private val orderClause = new StringBuilder()
  private var paginationClause: String = null

  def reset(sql: String): Unit = {
    str.clear()
    str.append(sql)
    whereClause.clear()
    orderClause.clear()
  }

  def where(expr: String): SimpleSelectQuery = {
    if (whereClause.nonEmpty)
      whereClause.append(" AND ")

    whereClause.append(expr)

    this
  }

  def whereNot(expr: String): SimpleSelectQuery = {
    where("(NOT " + expr + ")")
  }

  def whereIsNull(expr: String): SimpleSelectQuery = {
    where(expr + " IS NULL")
  }

  def whereIsNotNull(expr: String): SimpleSelectQuery = {
    where(expr + " IS NOT NULL")
  }

  def someColumnsLike(columns: Array[String]): SimpleSelectQuery = {
    val str = new StringBuilder()
    for (col <- columns) {
      if (str.nonEmpty)
        str.append(" OR ")

      str.append("LOWER(").append(col).append(") LIKE '%' || LOWER(?) || '%'")
    }
    where(str.toString())

    this
  }

  def hasWhereClause: Boolean =
    whereClause.nonEmpty

  def or(expr2: String): SimpleSelectQuery = {
    if (whereClause.isEmpty) {
      where(expr2)
    } else {
      val s = whereClause.toString()
      whereClause.clear()
      whereClause.append("((").append(s).append(")").append(" OR (").append(expr2).append("))")
    }

    this
  }

  def hasOrderBy: Boolean =
    orderClause.nonEmpty

  def orderBy(columnName: String, desc: Boolean = false): SimpleSelectQuery = {
    if (orderClause.nonEmpty)
      orderClause.append(", ")

    orderClause.append(columnName)
    if (desc)
      orderClause.append(" DESC ")

    this
  }

  def orderBy(sorting: Array[SortColumn], columnNameMapper: String => String): SimpleSelectQuery = {
    if (sorting != null) {
      sorting.foreach(sc => orderBy(columnNameMapper(sc.columnName), !sc.ascending))
    }

    this
  }

  def paginate(pagination: Pagination): SimpleSelectQuery = {
    if (pagination != null) {
      paginationClause = " LIMIT " + pagination.limit + " OFFSET " + ((pagination.page - 1) * pagination.limit)
    }

    this
  }

//  def applyQueryParamsWhereClauseOnly(queryParams: QueryParams, filterColumns: Array[String]): Unit = {
//    if (filterColumns == null || filterColumns.isEmpty) {
//      if (queryParams.filter != null) {
//        throw new InvalidParamException("filter")
//      }
//    }
//
//    if (queryParams.filter != null)
//      someColumnsLike(filterColumns)
//  }
//
//  def applyQueryParams(queryParams: QueryParams, filterColumns: Array[String], addOrderByAndPagination: Boolean, orderByColumnNameMapper: String => String): Unit = {
//    applyQueryParamsWhereClauseOnly(queryParams, filterColumns)
//
////    if (filterColumns == null || filterColumns.isEmpty) {
////      if (queryParams.filter != null) {
////        throw new InvalidParamException("filter")
////      }
////    }
////
////    if (queryParams.filter != null)
////      someColumnsLike(filterColumns)
//
//    if (addOrderByAndPagination) {
//      orderBy(queryParams.sorting, orderByColumnNameMapper)
//      paginate(queryParams.pagination)
//    }
//  }


  override def toString: String = toSql

  def toSql: String = {
    toSql(addOrderByClause = true, addPaginationClause = true)
//    val main = str.toString()
//    val where = if (whereClause.isEmpty) "" else " WHERE " + whereClause.toString()
//    val orderBy = if (orderClause.isEmpty) "" else " ORDER BY " + orderClause.toString()
//    val page = if (paginationClause == null) "" else paginationClause
//
//    main + where + orderBy + page
  }

  def toSql(addOrderByClause: Boolean, addPaginationClause: Boolean): String = {
    val r = new StringBuilder
    r.append(str)
    if (whereClause.nonEmpty)
      r.append(" WHERE ").append(whereClause)

    if (addOrderByClause && orderClause.nonEmpty)
      r.append(" ORDER BY ").append(orderClause)

    if (addPaginationClause && (paginationClause != null))
      r.append(paginationClause)

    r.toString()
  }

  def toSqlWithTotalRowCount: String = {
    if (paginationClause == null)
      return toSql


    val main = str.toString()
    val where = if (whereClause.isEmpty) "" else " WHERE " + whereClause.toString()
    val orderBy = if (orderClause.isEmpty) "" else " ORDER BY " + orderClause.toString()
    val page = if (paginationClause == null) "" else paginationClause

    //    if (page != null) {
    //      val s = insertFullCountColumn(main)

    s"""WITH cte AS (
          $main
          $where
          $orderBy
        )
        SELECT * FROM (
          TABLE cte
          $orderBy
          $page
        ) sub
        RIGHT JOIN (SELECT count(1) FROM cte) c(_total_row_count_) ON true
        """
    //    } else
    //      main + where + orderBy + page
  }

}


object SimpleSelectQuery {
  def apply(sql: String): SimpleSelectQuery =
    new SimpleSelectQuery(sql)


  def mapColumnName(columnName: String): String = {
    val r = new StringBuilder()

    for (c <- columnName) {
      if (c >= 'A' && c <= 'Z') {
        r.append('_').append(c.toLower)
      } else
        r.append(c)
    }

    r.toString
  }
}
