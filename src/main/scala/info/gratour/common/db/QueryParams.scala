package info.gratour.common.db

import java.time.{LocalDate, LocalDateTime, OffsetDateTime}
import java.time.format.DateTimeFormatter

import com.sun.istack.internal.Nullable
import com.typesafe.scalalogging.Logger
import info.gratour.common.db.schema._
import info.gratour.common.db.sqlgen.FieldResolver
import info.gratour.common.error.ErrorWithCode
import info.gratour.common.lang.Flag
import info.gratour.common.utils.StringUtils
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.schema.Column
import net.sf.jsqlparser.statement.select.{PlainSelect, Select, SelectExpressionItem}
import scalikejdbc.{AutoSession, DBSession, SQL, WrappedResultSet}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag


case class Pagination(
                       limit: Int,
                       page: Int // 1 based
                     ) {
  def offset: Int = (page - 1) * limit
}


case class SearchConditionSpec(
                                paramName: String,
                                dataType: FieldDataType,
                                required: Boolean = false,
                                constraint: FieldConstraint = null,
                                predicationOverride: Array[Predication] = null
                              ) {

  case class Phase1ParseResult(
                                predication1: Predication,
                                values: Array[String],
                                predication2: Predication)

  def parse1(text: String): Phase1ParseResult = {
    val EXPECT_PREDICATION_LEFT = 1
    val EXPECT_VALUES = 2
    val END = 3

    var state = EXPECT_PREDICATION_LEFT

    val str = new StringBuilder
    val values = ArrayBuffer.empty[String]
    var predication1: Predication = null
    var predication2: Predication = null
    var escaped = false


    text.chars().forEach(c => {
      val ch = c.toChar
      state match {
        case EXPECT_PREDICATION_LEFT =>
          ch match {
            case '(' =>
              if (dataType.isText)
                str.append(ch)
              else if (dataType.isBool)
                throw ErrorWithCode.invalidParam(paramName)
              else
                predication1 = Predication.GREAT

            case '[' =>
              if (dataType.isText)
                str.append(ch)
              else if (dataType.isBool)
                throw ErrorWithCode.invalidParam(paramName)
              else
                predication1 = Predication.GREAT_EQUAL

            case '\\' =>
              if (dataType.isText)
                escaped = true
              else
                throw ErrorWithCode.invalidParam(paramName)

            case '%' =>
              if (dataType.isText) {
                predication1 = Predication.START_WITH
              } else
                throw ErrorWithCode.invalidParam(paramName)

            case _ =>
              str.append(ch)
          }

          state = EXPECT_VALUES

        case EXPECT_VALUES =>
          if (escaped) {
            if (ch == '%')
              str.append('\\')
            str.append(ch)
            escaped = false
          } else {
            ch match {
              case ')' =>
                if (dataType.isText)
                  str.append(ch)
                else if (dataType.isBool)
                  throw ErrorWithCode.invalidParam(paramName)
                else {
                  if (predication1 == null)
                    predication1 = Predication.LESS
                  else
                    predication2 = Predication.LESS
                  state = END
                }

              case ']' =>
                if (dataType.isText)
                  str.append(ch)
                else if (dataType.isBool)
                  throw ErrorWithCode.invalidParam(paramName)
                else {
                  if (predication1 == null)
                    predication1 = Predication.LESS_EQUAL
                  else
                    predication2 = Predication.LESS_EQUAL
                  state = END
                }

              case ' ' =>
                if (dataType.isText)
                  str.append(ch)
                else {
                  // ignore it
                }

              case '%' =>
                if (dataType.isText) {
                  if (predication1 == null) {
                    predication1 = Predication.END_WITH
                    state = END // not allow any other follow the data
                  } else if (predication1 == Predication.START_WITH) {
                    predication1 = Predication.INCLUDE
                    state = END // not allow any other follow the data
                  } else
                    throw ErrorWithCode.invalidParam(paramName)
                } else
                  throw ErrorWithCode.invalidParam(paramName)

              case ',' =>
                if (dataType.isBool)
                  throw ErrorWithCode.invalidParam(paramName)

                if (dataType.isText)
                  str.append(ch)
                else {
                  values += str.toString()
                  str.setLength(0)
                }


              case '\\' =>
                if (dataType.isText)
                  escaped = true
                else
                  throw ErrorWithCode.invalidParam(paramName)


              case _ =>
                str.append(ch)
            }
          }

        case END =>
          // not allow any character after state END
          throw ErrorWithCode.invalidParam(paramName)
      }

    })

    if (str.nonEmpty)
      values += str.toString()

    if (predication1 == null)
      predication1 = Predication.EQUAL

    Phase1ParseResult(predication1, values.toArray, predication2)
  }

  def check(c: SearchCondition, dbColumnNames: Array[String]): ParsedSearchCondition = {
    if (required) {
      if (c == null)
        throw ErrorWithCode.invalidParam(paramName)
    }

    if (c == null)
      return null

    val phase1ParseResult = parse1(c.textToSearch)
    if (phase1ParseResult.values.isEmpty)
      throw ErrorWithCode.invalidParam(paramName)

    // check value count and predication

    if (phase1ParseResult.values.length > 2)
      throw ErrorWithCode.invalidParam(paramName)

    if (phase1ParseResult.predication2 == null) {
      if (phase1ParseResult.values.length > 1)
        throw ErrorWithCode.invalidParam(paramName)
    } else {
      if (phase1ParseResult.values.length != 2)
        throw ErrorWithCode.invalidParam(paramName)
    }

    if (phase1ParseResult.predication1 == Predication.EQUAL || Predication.isLike(phase1ParseResult.predication1)) {
      if (phase1ParseResult.values.length > 1)
        throw ErrorWithCode.invalidParam(paramName)
    }

    // check dataType and predication
    if (!dataType.supportPredication(phase1ParseResult.predication1))
      throw ErrorWithCode.invalidParam(paramName)

    val parsedValues = ArrayBuffer.empty[Object]
    var v: Object = null

    dataType match {
      case FieldDataType.BOOL =>
        if (phase1ParseResult.values.length != 1) // only support EQUAL, IS_NULL, IS_NOT_NULL
          throw ErrorWithCode.invalidParam(paramName)
        v = StringUtils.tryParseBool(phase1ParseResult.values(0))
        if (v == null)
          throw ErrorWithCode.invalidParam(paramName)
        parsedValues += v

      case FieldDataType.SMALL_INT =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseShort(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.INT =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseInt(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.BIGINT =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseLong(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.TEXT =>
        phase1ParseResult.values.foreach(parsedValues += _)

      case FieldDataType.DECIMAL =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseDecimal(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }


      case FieldDataType.FLOAT =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseFloat(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.DOUBLE =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseDouble(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.LOCAL_DATE =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseLocalDate(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.LOCAL_DATETIME =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseLocalDateTime(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.OFFSET_DATETIME =>
        for (s <- phase1ParseResult.values) {
          v = StringUtils.tryParseOffsetDateTime(s)
          if (v == null)
            throw ErrorWithCode.invalidParam(paramName)

          parsedValues += v
        }

      case FieldDataType.BINARY =>
        throw ErrorWithCode.invalidParam(paramName) // binary data type does not support search
    }

    val values = parsedValues.toArray
    if (constraint != null)
      constraint.checkValue(paramName, values)


    ParsedSearchCondition(this, dbColumnNames, phase1ParseResult.predication1, phase1ParseResult.predication2, values)
  }

}

trait SearchConditionTrait {
  def paramName: String
}

case class SearchCondition(
                            paramName: String,
                            textToSearch: String
                          ) extends SearchConditionTrait

case class ParsedSearchCondition(
                                  spec: SearchConditionSpec,
                                  dbColumnNames: Array[String],
                                  predication: Predication,
                                  predication2: Predication,
                                  parsedValues: Array[Object]) extends SearchConditionTrait {
  override def paramName: String = spec.paramName
}

case class SearchConditions(
                             conditions: Array[SearchCondition]
                           ) {
  def isEmpty: Boolean = conditions == null || conditions.length == 0

  def find(paramName: String): SearchCondition =
    if (isEmpty) null
    else conditions.find(_.paramName == paramName).orNull
}

object SearchConditions {
  def apply(conditions: Array[SearchCondition]): SearchConditions = new SearchConditions(conditions)

  def apply(conditions: java.util.List[SearchCondition]): SearchConditions = {
    val r: Array[SearchCondition] = new Array(conditions.size())
    new SearchConditions(conditions.toArray(r))
  }
}

case class ParsedSearchConditions(
                                   conditions: Array[ParsedSearchCondition]
                                 ) {
  def isEmpty: Boolean = conditions == null || conditions.isEmpty
}

case class SortColumn(columnName: String, ascending: Boolean)

case class QueryParams(
                        conditions: SearchConditions,
                        pagination: Pagination,
                        sortColumns: Array[SortColumn]
                      ) {
  def hasConditions: Boolean = conditions != null && !conditions.isEmpty

  def findCondition(paramName: String): SearchCondition =
    if (conditions != null) conditions.find(paramName)
    else null

  def hasSortColumns: Boolean = sortColumns != null && !sortColumns.isEmpty
}

object QueryParams {

  def parseSortColumns(s: String): Array[SortColumn] = {
    if (s == null || s.isEmpty)
      null
    else {
      val v = s.split(",")
      var count = v.length
      val r = ArrayBuffer.empty[SortColumn]
      v.foreach(field => {
        val idx = field.indexOf('$')
        if (idx == 0)
          throw ErrorWithCode.invalidParam("__sortBy")

        val (fieldName, asc) =
          if (idx > 0) {
            val fieldName = field.substring(0, idx)
            val indicator = field.substring(idx + 1)

            val asc = indicator.toLowerCase match {
              case "asc" =>
                true
              case "desc" =>
                false
              case _ =>
                throw ErrorWithCode.invalidParam("__sortBy")
            }

            (fieldName, asc)
          } else {
            (field, true)
          }

        r += SortColumn(fieldName, asc)
      })

      r.toArray
    }
  }

  def apply(conditions: SearchConditions,
            pagination: Pagination,
            sortColumns: Array[SortColumn]): QueryParams = new QueryParams(conditions, pagination, sortColumns)



  val NONE: QueryParams = new QueryParams(null, null, null)
}

class QueryParamsBuilder() {

  private var conditions: ArrayBuffer[SearchCondition] = _
  private var pagination: Pagination = _
  private var sortColumns: ArrayBuffer[SortColumn] = _

  def condition(paramName: String, paramValue: String): QueryParamsBuilder = {
    if (conditions == null)
      conditions = ArrayBuffer.empty[SearchCondition]
    conditions += SearchCondition(paramName, paramValue)
    this
  }

  def condition(paramName: String, paramValue: Boolean): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: Int): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: Long): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: Float): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: Double): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: BigDecimal): QueryParamsBuilder =
    condition(paramName, paramValue.toString)
  def condition(paramName: String, paramValue: LocalDate): QueryParamsBuilder =
    condition(paramName, paramValue.format(DateTimeFormatter.ISO_LOCAL_DATE))
  def condition(paramName: String, paramValue: LocalDateTime): QueryParamsBuilder =
    condition(paramName, paramValue.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
  def condition(paramName: String, paramValue: OffsetDateTime): QueryParamsBuilder =
    condition(paramName, paramValue.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))

  def pagination(limit: Int, page: Int): QueryParamsBuilder = {
    this.pagination = new Pagination(limit, page)
    this
  }

  def sortColumn(columnName: String, ascending: Boolean = true): QueryParamsBuilder = {
    if (sortColumns == null)
      sortColumns = ArrayBuffer.empty[SortColumn]

    sortColumns += SortColumn(columnName, ascending)
    this
  }

  def build(): QueryParams = {
    val searchConditions =
      if (conditions != null && conditions.nonEmpty)
        SearchConditions(conditions.toArray)
      else
        null


    QueryParams(
      searchConditions,
      pagination,
      if (sortColumns != null && sortColumns.nonEmpty) sortColumns.toArray else null)
  }
}

object QueryParamsBuilder {
  def apply(): QueryParamsBuilder = new QueryParamsBuilder
}

case class ParsedQueryParams[T <: AnyRef](
                                           spec: QueryParamsSpec[T],
                                           select: String,
                                           conditions: ParsedSearchConditions,
                                           pagination: Pagination,
                                           sortColumns: Array[SortColumn]
                                         )(implicit tag: ClassTag[T]) {

  private val logger = Logger(ParsedQueryParams.getClass.getName)

  private def hasConditions: Boolean = conditions != null && !conditions.isEmpty

  private def toSql: String = {
    val where = if (conditions != null && !conditions.isEmpty) {
      val where = new StringBuilder()
      where.append(" WHERE ")
      var first = true

      conditions.conditions.foreach(cond => {
        if (first)
          first = false
        else
          where.append(" AND ")


        val expressions = cond.dbColumnNames.map(columnName => {
          val cond1 = cond.predication match {
            case Predication.EQUAL =>
              if (cond.predication2 != null)
                throw ErrorWithCode.internalError("cond.predication2 != null")
              columnName + " = ?"

            case Predication.LESS =>
              columnName + " < ?"

            case Predication.LESS_EQUAL =>
              columnName + " <= ?"

            case Predication.GREAT =>
              columnName + " > ?"

            case Predication.GREAT_EQUAL =>
              columnName + " >= ?"

            case Predication.START_WITH =>
              if (cond.predication2 != null)
                throw ErrorWithCode.internalError("cond.predication2 != null")
              columnName + " ILIKE '%' || ?"

            case Predication.INCLUDE =>
              if (cond.predication2 != null)
                throw ErrorWithCode.internalError("cond.predication2 != null")
              columnName + " ILIKE '%' || ? || '%'"

            case Predication.END_WITH =>
              if (cond.predication2 != null)
                throw ErrorWithCode.internalError("cond.predication2 != null")
              columnName + " ILIKE ? || '%'"

            case _ =>
              throw ErrorWithCode.internalError(s"Unhandled case ${cond.predication}.")

          }

          val cond2 =
            if (cond.predication2 != null) {
              cond.predication2 match {
                case Predication.LESS =>
                  columnName + "< ?"
                case Predication.LESS_EQUAL =>
                  columnName + "<= ?"
                case Predication.GREAT =>
                  columnName + "> ?"
                case Predication.GREAT_EQUAL =>
                  columnName + ">= ?"

                case _ =>
                  throw ErrorWithCode.internalError(s"Unhandled case ${cond.predication2}.")
              }
            } else
              null

          if (cond2 == null)
            cond1
          else {
            "(" + cond1 + " AND " + cond2 + ")"
          }
        }) // fieldNames.map(fieldName => {

        if (expressions.length == 1)
          where.append(expressions(0))
        else {
          val flag = Flag(true)
          val str = new StringBuilder
          expressions.foreach(expr => {
            if (flag.value)
              flag.value = false
            else
              str.append(" OR ")

            str.append(expr)
          })

          where.append(str.toString())
        }

      }) // conditions.conditions.foreach(cond => {

      where.toString()
    } else
      ""

    val orderBy =
      if (sortColumns != null && sortColumns.nonEmpty) {
        val str = new StringBuilder()
        str.append(" ORDER BY ");
        var first = true
        sortColumns.foreach(sc => {
          if (first)
            first = false
          else
            str.append(", ")

          str.append(spec.fieldNameMapper.toFirstDbColumnName(sc.columnName))
          if (!sc.ascending)
            str.append(" DESC")
        })

        str.toString()
      } else ""

    if (pagination == null) {
      select + where.toString + orderBy
    } else {
      s"""
          WITH cte AS (
           $select
           $where
           $orderBy
          )
          SELECT * FROM (
           TABLE cte
           $orderBy
           LIMIT ${pagination.limit} OFFSET ${pagination.offset}
          ) sub
          RIGHT JOIN (SELECT count(1) FROM cte) c(__rc__) ON TRUE
        """
    }
  }

  val selectSql = {
    val sql = toSql
    logger.whenDebugEnabled {
      logger.debug("SQL => " + sql)
    }
    sql
  }

  class HookedMapper[A <: AnyRef](val underlying: ScalikeMapper[A])(implicit classTag: ClassTag[A]) extends ScalikeMapper[A] {

    private var trc: java.lang.Long = _

    override def apply(v1: WrappedResultSet): A = {
      if (trc == null) {
        trc = v1.long("__rc__")
        if (trc == 0L)
          return null.asInstanceOf[A]
      }

      underlying.apply(v1)
    }

    def totalRecordCount: Long =
      if (trc != null)
        trc
      else
        0L
  }

  def query()(implicit session: DBSession): QueryResult[T] = {
    var sql = SQL(selectSql)

    if (hasConditions) {
      val values = ArrayBuffer.empty[Object]

      conditions.conditions.foreach(c => {

        // each parsedValues for each mapped column
        c.dbColumnNames.foreach(_ => {
          values ++= c.parsedValues
        })
      })
      sql = sql.bind(values: _*)
    }

    if (pagination != null) {
      val hookedMapper = new HookedMapper[T](spec.MAPPER)
      val list: List[T] = sql.map(hookedMapper)
        .list()
        .apply()

      if (hookedMapper.totalRecordCount == 0)
        QueryResult(new Array[T](0), 0)
      else
        QueryResult(list.toArray, hookedMapper.totalRecordCount)
    } else {
      val list: List[T] = sql.map(spec.MAPPER)
        .list()
        .apply()

      QueryResult(list.toArray, list.size)
    }

  }
}

/**
 * 查询条件规范
 */
case class QueryParamsSpec[T <: AnyRef](
                                         select: String,
                                         entryClass: Class[T],
                                         searchConditionSpecs: Array[SearchConditionSpec],
                                         supportedOrderColumns: Array[String],
                                         paginationSupport: PaginationSupportSpec = PaginationSupportSpec.OPTIONAL,
                                         fieldNameMapper: FieldNameMapper = FieldNameMapper.INSTANCE,
                                         mapper: ScalikeMapper[T] = null
                                       )(implicit session: DBSession, classTag: ClassTag[T]) {

  val MAPPER: ScalikeMapper[T] =
    if (mapper != null)
      mapper
    else
      MapperBuilder.build(select, entryClass, fieldNameMapper)

  def supportOrderBy(columnName: String): Boolean = supportedOrderColumns != null && supportedOrderColumns.contains(columnName)

  def check(queryParams: QueryParams): ParsedQueryParams[T] = {
    val parsedSearchConditions = ArrayBuffer.empty[ParsedSearchCondition]

    searchConditionSpecs.foreach(
      spec => {
        val cond = queryParams.findCondition(spec.paramName)
        if (cond == null) {
          if (spec.required)
            throw ErrorWithCode.invalidParam(spec.paramName)
        } else {
          val dbColumnNames = fieldNameMapper.checkedToDbColumnNames(spec.paramName)
          val parsedCond = spec.check(cond, dbColumnNames)
          parsedSearchConditions += parsedCond
        }
      }
    )

    if (queryParams.hasSortColumns)
      queryParams.sortColumns.foreach(sc => {
        if (!supportOrderBy(sc.columnName))
          throw ErrorWithCode.invalidParam(s"__sortBy(${sc.columnName})")
      })

    paginationSupport match {
      case PaginationSupportSpec.OPTIONAL =>
      case PaginationSupportSpec.MUST_SPECIFIED =>
        if (queryParams.pagination == null)
          throw ErrorWithCode.invalidParam("__limit/__page")

      case PaginationSupportSpec.NOT_SUPPORT =>
        if (queryParams.pagination != null)
          throw ErrorWithCode.invalidParam("__limit/__page")
    }

    ParsedQueryParams(this, select, new ParsedSearchConditions(parsedSearchConditions.toArray), queryParams.pagination, queryParams.sortColumns)
  }
}



class QueryParamsSpecBuilder[T <: AnyRef](
                                           val select: String,
                                           val entryClass: Class[T],
                                           val fieldResolver: FieldResolver,
                                           val mapper: ScalikeMapper[T])(implicit classTag: ClassTag[T]) {

  private val searchConditionSpecList = ArrayBuffer.empty[SearchConditionSpec]
  private val orderByColumns = ArrayBuffer.empty[String]
  private var paginationSupportSpec = PaginationSupportSpec.NOT_SUPPORT

  def condition(paramName: String,
                required: Boolean = false,
                constraint: FieldConstraint = null,
                predicationOverride: Array[Predication] = null): QueryParamsSpecBuilder[T] = {

    val dataType = {
      val columnName = fieldResolver.getFieldNameMapper.toFirstDbColumnName(paramName)

      val dataType = fieldResolver.getFieldDataType(columnName)
      if (dataType == null)
        throw ErrorWithCode.invalidParam(s"Field `$columnName` is not defined.")
      dataType
    }

    searchConditionSpecList += SearchConditionSpec(paramName, dataType, required, constraint, predicationOverride)
    this
  }

  /**
   * define special search condition which has no db-mapping
   *
   * @param paramName
   * @param required
   * @param fieldDataType
   * @param constraint
   * @param predicationOverride
   * @return
   */
  def specialCondition(paramName: String,
                       fieldDataType: FieldDataType,
                       required: Boolean = false,
                       constraint: FieldConstraint = null,
                       predicationOverride: Array[Predication] = null): QueryParamsSpecBuilder[T] = {

    searchConditionSpecList += SearchConditionSpec(paramName, fieldDataType, required, constraint, predicationOverride)
    this
  }

  def conditionsFromSelect(@Nullable fieldDataTypeResolver: Map[String, FieldDataType]): QueryParamsSpecBuilder[T] = {
    val parsedSelect = CCJSqlParserUtil.parse(select).asInstanceOf[Select]
    val plainSelect = parsedSelect.getSelectBody.asInstanceOf[PlainSelect]
    plainSelect.getSelectItems.forEach {
      case sei: SelectExpressionItem =>
        sei.getExpression match {
          case c: Column =>
            val columnName = if (sei.getAlias != null) {
              sei.getAlias.getName
            } else
              c.getColumnName

            val fieldName = fieldResolver.getFieldNameMapper.toApiFieldName(columnName)
            if (fieldDataTypeResolver == null)
              condition(fieldName)
            else {
              val dataType1 = fieldDataTypeResolver.get(fieldName).orNull
              val dataType = if (dataType1 != null) dataType1 else fieldResolver.getFieldDataType(columnName)
              specialCondition(fieldName, dataType)
            }
        }
    }

    this
  }

  def orderBy(fieldName: String): QueryParamsSpecBuilder[T] = {
    orderByColumns += fieldName
    this
  }

  def orderBy(fieldNames: String*): QueryParamsSpecBuilder[T] = {
    fieldNames.foreach(orderByColumns += _)
    this
  }

  def orderByAllParams(): QueryParamsSpecBuilder[T] = {
    searchConditionSpecList.foreach(orderByColumns += _.paramName)
    this
  }

  def paginationOpt(): QueryParamsSpecBuilder[T] = {
    paginationSupportSpec = PaginationSupportSpec.OPTIONAL
    this
  }

  def paginationMust(): QueryParamsSpecBuilder[T] = {
    paginationSupportSpec = PaginationSupportSpec.MUST_SPECIFIED
    this
  }

  def build()(implicit classTag: ClassTag[T], session: DBSession = AutoSession): QueryParamsSpec[T] =
    QueryParamsSpec(
      select,
      entryClass,
      searchConditionSpecList.toArray,
      orderByColumns.toArray,
      paginationSupportSpec,
      fieldResolver.getFieldNameMapper,
      mapper
    )

}


