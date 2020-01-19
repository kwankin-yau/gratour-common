package info.gratour.common.db

import java.lang.reflect.{Field, Modifier}
import java.sql.Types
import java.util.concurrent.ConcurrentHashMap

import info.gratour.common.db.schema.FieldNameMapper
import info.gratour.common.error.ErrorWithCode
import info.gratour.common.lang.Reflections
import info.gratour.common.utils.{CommonUtils, StringUtils}
import net.sf.jsqlparser.parser.{CCJSqlParser, CCJSqlParserUtil}
import net.sf.jsqlparser.schema.{Column, Table}
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.statement.select.{AllColumns, AllTableColumns, PlainSelect, Select, SelectExpressionItem}
import scalikejdbc.{AutoSession, DBSession, WrappedResultSet}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.immutable
import scala.reflect.ClassTag


object MapperBuilder {

  case class Col(name: String, originName: String, tableName: String)

  def parse(selectSql: String): ArrayBuffer[Col] = {
    val select = CCJSqlParserUtil.parse(selectSql).asInstanceOf[Select].getSelectBody.asInstanceOf[PlainSelect]
    val aliasMapper = mutable.Map[String, String]()

    val primaryTableName = select.getFromItem.asInstanceOf[Table].getName
    if (select.getFromItem.getAlias != null)
      aliasMapper += (select.getFromItem.getAlias.getName -> primaryTableName)

    val joins = select.getJoins
    if (joins != null) {
      joins.forEach(
        join => {
          val t = join.getRightItem.asInstanceOf[Table]
          if (t.getAlias != null)
            aliasMapper += (t.getAlias.getName -> t.getName)
        }
      )
    }

    val r = ArrayBuffer.empty[Col]

    select.getSelectItems.forEach {
      case item: SelectExpressionItem =>
        item.getExpression match {
          case col: Column =>
            val alias = if (item.getAlias != null) item.getAlias.getName else col.getColumnName
            val t = if (col.getTable != null) col.getTable.getName else null
            val tableOfColumn = if (t != null) aliasMapper.getOrElse(t, t) else primaryTableName

            r += Col(alias, col.getColumnName, tableOfColumn)
          case _ =>
        }

      case _ =>
      // do nothing
    }

    r
  }

  def build[T](selectSql: String, entryClass: Class[T], fieldNameMapper: FieldNameMapper = FieldNameMapper.INSTANCE)(implicit session: DBSession = AutoSession): WrappedResultSet => T = {
    type SetMethod = (T, WrappedResultSet) => Unit;
    type Setter = (T, Field, WrappedResultSet) => Unit;
    val stmt = CCJSqlParserUtil.parse(selectSql)
    val cols = parse(selectSql)

    def indexOf(fieldName: String): Int = {
      val columnName = fieldNameMapper.toFirstDbColumnName(fieldName)
      cols.indexWhere(col => col.name == columnName)
    }

    val setMethodsBuffer = ArrayBuffer.empty[SetMethod]

    CommonUtils.getInstanceFields(entryClass).foreach(f => {
      if (!Modifier.isTransient(f.getModifiers)) {
        val idx = indexOf(f.getName)
        if (idx >= 0) {
          val columnIndex = idx + 1

          val typ = f.getType
          f.setAccessible(true)
          val setter: Setter = typ match {
            case Reflections.JBoolean =>
              (t, f, rs) =>
                f.set(t, rs.booleanOpt(columnIndex).orNull)

            case Reflections.JBooleanPrimitive =>
              (t, f, rs) =>
                f.setBoolean(t, rs.boolean(columnIndex))

            case Reflections.JShort =>
              (t, f, rs) =>
                f.set(t, rs.shortOpt(columnIndex).orNull)

            case Reflections.JShortPrimitive =>
              (t, f, rs) =>
                f.setShort(t, rs.short(columnIndex))

            case Reflections.JInteger =>
              (t, f, rs) =>
                f.set(t, rs.intOpt(columnIndex).orNull)

            case Reflections.JIntegerPrimitive =>
              (t, f, rs) =>
                f.setInt(t, rs.int(columnIndex))

            case Reflections.JLong =>
                (t, f, rs) =>
                  f.set(t, rs.longOpt(columnIndex).orNull)

            case Reflections.JLongPrimitive =>
              (t, f, rs) =>
                f.setLong(t, rs.long(columnIndex))

            case Reflections.JFloat =>
                (t, f, rs) =>
                  f.set(t, rs.floatOpt(columnIndex))

            case Reflections.JFloatPrimitive =>
              (t, f, rs) =>
                f.setFloat(t, rs.float(columnIndex))

            case Reflections.JDouble =>
                (t, f, rs) =>
                  f.set(t, rs.doubleOpt(columnIndex))

            case Reflections.JDoublePrimitive =>
              (t, f, rs) =>
                f.setDouble(t, rs.double(columnIndex))

            case Reflections.JBigDecimal =>
              (t, f, rs) =>
                f.set(t, rs.bigDecimal(columnIndex))

            case Reflections.JString =>
              (t, f, rs) =>
                f.set(t, rs.string(columnIndex))

            case Reflections.JLocalDate =>
              (t, f, rs) =>
                f.set(t, rs.localDate(columnIndex))

            case Reflections.JLocalDateTime =>
              (t, f, rs) =>
                f.set(t, rs.localDateTime(columnIndex))

            case Reflections.JOffsetDateTime =>
              (t, f, rs) =>
                f.set(t, rs.offsetDateTime(columnIndex))

            case Reflections.JByteArray =>
              (t, f, rs) => {
                val bytes = rs.bytes(columnIndex)
                f.set(t, bytes)
              }

            case _ =>
              (t, f, rs) =>
                throw ErrorWithCode.internalError(s"Unsupported entry field type: ${typ.getName}($typ).")

          }

          val method: SetMethod = (t: T, rs: WrappedResultSet) => {
            setter(t, f, rs)
          }

          setMethodsBuffer += method
        }
      }
    })

    val setMethods = setMethodsBuffer.toArray

    (wrappedResultSet) => {
      val e = entryClass.newInstance()
      setMethods.foreach(m => {
        m(e, wrappedResultSet)
      })
      e
    }
  }

}
