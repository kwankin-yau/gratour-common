package info.gratour.common.db

import java.sql.{PreparedStatement, ResultSet, Types}
import java.time.{LocalDate, LocalDateTime, LocalTime, OffsetDateTime}
import java.util
import java.util.Locale

import com.google.gson.{Gson, GsonBuilder}
import info.gratour.common.Consts
import info.gratour.common.error.{ErrorWithCode, Errors}
import info.gratour.common.lang.{IncIndex, InvalidParamException}
import info.gratour.common.rest.Reply
import org.apache.commons.lang3.StringUtils
import scalikejdbc.{DB, DBSession, WrappedResultSet, using}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.macros.whitebox
import scalikejdbc._

trait DbHelper {

  def setConstraintsDeferred()(implicit session: DBSession): Unit = {
    sql"""
          SET CONSTRAINTS ALL DEFERRED;
      """
      .execute()
      .apply()
  }

  def convert[T](accMapper: SequenceColumnAccessor => T): WrappedResultSet => T =
    (wrappedResultSet: WrappedResultSet) => {
      val acc = new SequenceColumnAccessor(wrappedResultSet.underlying)
      accMapper(acc)
    }


  trait RowMapper2[T] {
    def map(accessor: SequenceColumnAccessor): T
  }

  def mapRow[T](rs: ResultSet, mapper: RowMapper2[T]): T = {
    val accessor = SequenceColumnAccessor(rs)
    mapper.map(accessor)
  }

  def mapRows[T](rs: ResultSet, mapper: RowMapper2[T]): java.util.List[T] = {
    val r = new util.ArrayList[T]()
    val accessor = new SequenceColumnAccessor(rs)
    while (rs.next()) {
      accessor.reset()

      r.add(mapper.map(accessor))
    }

    r
  }

  def valuesToSqlPlaceHolders(values: Array[String]): String =
    if (values != null && !values.isEmpty)
      values.map(_ => "?").mkString(",")
    else
      null


  def preparedExec[T](sql: String, binding: StatementBinder => Unit, statementProcessor: PreparedStatement => T)(implicit session: DBSession): T = {
    using(session.connection.prepareStatement(sql)) { st =>
      val binder = new StatementBinder(st)
      binding(binder)
      statementProcessor(st)
    }
  }

  type MultiBinder[T] = (T, StatementBinder) => Unit

  def preparedBatchUpdate[T](sql: String, values: Seq[T], multiBinder: MultiBinder[T])(implicit session: DBSession): Seq[Int] = {
    val r: ArrayBuffer[Int] = ArrayBuffer()

    using(session.connection.prepareStatement(sql)) { st =>
      val binder = new StatementBinder(st)

      values.foreach(v => {
        binder.idx.index = 0
        multiBinder(v, binder)
        st.executeUpdate()
      })

    }

    r
  }

  def executeQry(st: PreparedStatement): SequenceColumnAccessor = {
    val rs = st.executeQuery()
    new SequenceColumnAccessor(rs)
  }

}


object DbHelper {
  val TOTAL_ROW_COUNT_COLUMN_NAME = "_rc_"

  def inSession[A](connProvider: ConnProvider, code: DBSession => A): A = {
    using(DB(connProvider.getConn)) { db =>
      db localTx[A] { implicit  session =>
        code(session)
      }
    }
  }

  def readOnlySession[A](connProvider: ConnProvider, code: DBSession => A): A = {
    using(DB(connProvider.getConn)) { db =>
      db readOnly { implicit session =>
        code(session)
      }
    }
  }
}

case class QueryResult[T <: Object](dataArray: Array[T], totalRecordCount: Long) {
  def toReply: Reply[T] = {
    new Reply[T](dataArray, totalRecordCount)
  }

  def isEmpty: Boolean = dataArray == null || dataArray.isEmpty
  def nonEmpty: Boolean = dataArray != null && dataArray.nonEmpty
  def first: Option[T] = if (nonEmpty) Some(dataArray(0)) else None
}

case class IdAndOffsetDateTime(id: String, offsetDateTime: OffsetDateTime)


trait DbSupport {

  val connProvider: ConnProvider

  protected[this] def inSession[A](code: DBSession => A): A = DbHelper.inSession(connProvider, code)

  protected[this] def readOnlySession[A](code: DBSession => A): A = DbHelper.readOnlySession(connProvider, code)

}

object SequenceColumnAccessor {
  def apply(rs: ResultSet): SequenceColumnAccessor = new SequenceColumnAccessor(rs)

  def apply(): SequenceColumnAccessor = new SequenceColumnAccessor(resultSet = null)


  val GSON: Gson = new GsonBuilder().create()
}

class SequenceColumnAccessor(val resultSet: ResultSet) {

  var rs: ResultSet = resultSet

  val colIndex: IncIndex = IncIndex()

  def next(): Boolean = {
    val r = rs.next()
    if (r)
      reset()

    r
  }

  def reset(): Unit = colIndex.index = 0

  def reset(resultSet: ResultSet): SequenceColumnAccessor = {
    rs = resultSet
    colIndex.index = 0
    this
  }

  def wasNull: Boolean = rs.wasNull()

  def str(): String = {
    rs.getString(colIndex.inc())
  }

  def small(): Short = {
    rs.getShort(colIndex.inc())
  }

  def smallOpt(): Option[Short] = {
    val r = rs.getShort(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def int(): Int = {
    rs.getInt(colIndex.inc())
  }

  def intOpt(): Option[Int] = {
    val r = rs.getInt(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def long(): Long = {
    rs.getLong(colIndex.inc())
  }

  def longOpt(): Option[Long] = {
    val r = rs.getLong(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def bool(): Boolean =
    rs.getBoolean(colIndex.inc())

  def boolOpt(): Option[Boolean] = {
    val r = rs.getBoolean(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def single(): Float =
    rs.getFloat(colIndex.inc())

  def singleOpt(): Option[Float] = {
    val r = rs.getFloat(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def double(): Double =
    rs.getDouble(colIndex.inc())

  def doubleOpt(): Option[Double] = {
    val r = rs.getDouble(colIndex.inc())
    if (rs.wasNull())
      None
    else
      Some(r)
  }

  def decimal(): BigDecimal =
    rs.getBigDecimal(colIndex.inc())

  def localDate(): LocalDate =
    rs.getObject(colIndex.inc(), classOf[LocalDate])

  def localTime(): LocalTime =
    rs.getObject(colIndex.inc(), classOf[LocalTime])

  def localDateTime(): LocalDateTime =
    rs.getObject(colIndex.inc(), classOf[LocalDateTime])

  def offsetDateTime(): OffsetDateTime =
    rs.getObject(colIndex.inc(), classOf[OffsetDateTime])

  def json[T >: AnyRef]()(implicit m: Manifest[T]): T = {
    val s = rs.getString(colIndex.inc())
    if (rs.wasNull())
      null
    else
      SequenceColumnAccessor.GSON.fromJson(s, m.runtimeClass.asInstanceOf[Class[T]])
  }

}


class StatementBinder(val st: PreparedStatement) {
  val idx = IncIndex()

  def setNull(sqlType: Int): Unit =
    st.setNull(idx.inc(), sqlType)

  def setBool(value: Boolean): Unit =
    st.setBoolean(idx.inc(), value)

  def setBoolOpt(value: Option[Boolean]): Unit =
    if (value.isDefined)
      setBool(value.get)
    else
      st.setNull(idx.inc(), Types.BOOLEAN)

  def setShort(value: Short): Unit =
    st.setShort(idx.inc(), value)

  def setShortOpt(value: Option[Short]): Unit =
    if (value.isDefined)
      setShort(value.get)
    else
      st.setNull(idx.inc(), Types.SMALLINT)

  def setInt(value: Int): Unit =
    st.setInt(idx.inc(), value)

  def setIntOpt(value: Option[Int]): Unit =
    if (value.isDefined)
      setInt(value.get)
    else
      setNull(Types.INTEGER)

  def setSingle(value: Float): Unit =
    st.setFloat(idx.inc(), value)

  def setSingleOpt(value: Option[Float]): Unit =
    if (value.isDefined)
      setSingle(value.get)
    else
      setNull(Types.FLOAT)

  def setDouble(value: Double): Unit =
    st.setDouble(idx.inc(), value)

  def setDoubleOpt(value: Option[Double]): Unit =
    if (value.isDefined)
      setDouble(value.get)
    else
      setNull(Types.DOUBLE)

  def setDecimal(value: java.math.BigDecimal): Unit =
    st.setBigDecimal(idx.inc(), value)

  def setString(value: String): Unit =
    st.setString(idx.inc(), value)

  def setLocalDate(value: LocalDate): Unit =
    if (value != null)
      st.setObject(idx.inc(), value)
    else
      setNull(Types.DATE)

  def setLocalTime(value: LocalTime): Unit =
    if (value != null)
      st.setObject(idx.inc(), value)
    else
      setNull(Types.TIME)

  def setOffsetDateTime(value: OffsetDateTime): Unit =
    if (value != null)
      st.setObject(idx.inc(), value)
    else
      setNull(Types.TIMESTAMP_WITH_TIMEZONE)

  import scala.reflect.runtime.universe._
  import scala.language.experimental.macros


  def set[T](value: T)(implicit tag: TypeTag[T]): Unit = {
    value match {
      case boolean: Boolean =>
        setBool(boolean)
      case short: Short =>
        setShort(short)
      case int: Int =>
        setInt(int)
      case single: Float =>
        setSingle(single)
      case double: Double =>
        setDouble(double)
      case string: String =>
        setString(string)

      case localDate: LocalDate =>
        setLocalDate(localDate)
      case localTime: LocalTime =>
        setLocalTime(localTime)
      case offsetDateTime: OffsetDateTime =>
        setOffsetDateTime(offsetDateTime)

      case jbool: java.lang.Boolean =>
        if (jbool != null)
          setBool(jbool.booleanValue())
        else
          setNull(Types.BOOLEAN)

      case jshort: java.lang.Short =>
        if (jshort != null)
          setShort(jshort.shortValue())
        else
          setNull(Types.SMALLINT)

      case jint: java.lang.Integer =>
        if (jint != null)
          setInt(jint.intValue())
        else
          setNull(Types.INTEGER)

      case jsingle: java.lang.Float =>
        if (jsingle != null)
          setSingle(jsingle.floatValue())
        else
          setNull(Types.FLOAT)

      case jdouble: java.lang.Double =>
        if (jdouble != null)
          setDouble(jdouble.doubleValue())
        else
          setNull(Types.DOUBLE)

      case jdec: java.math.BigDecimal =>
        setDecimal(jdec)

      case opt: Option[_] =>
        tag.tpe match {
          case TypeRef(_, _, args) =>
            val optArgType = args.head
            if (optArgType =:= info.gratour.common.Types.BoolType) {
              if (opt.isDefined)
                setBool(opt.get.asInstanceOf[Boolean])
              else
                setNull(Types.BOOLEAN)
            } else if (optArgType =:= info.gratour.common.Types.ShortType) {
              if (opt.isDefined)
                setShort(opt.get.asInstanceOf[Short])
              else
                setNull(Types.SMALLINT)
            } else if (optArgType =:= info.gratour.common.Types.IntType) {
              if (opt.isDefined)
                setInt(opt.get.asInstanceOf[Int])
              else
                setNull(Types.INTEGER)
            } else if (optArgType =:= info.gratour.common.Types.FloatType) {
              if (opt.isDefined)
                setSingle(opt.get.asInstanceOf[Float])
              else
                setNull(Types.FLOAT)
            } else if (optArgType =:= info.gratour.common.Types.DoubleType) {
              if (opt.isDefined)
                setDouble(opt.get.asInstanceOf[Double])
              else
                setNull(Types.DOUBLE)
            } else
              throw new RuntimeException("Unsupported element type: " + optArgType.toString)

        }


      case _ =>
        val tpe = tag.tpe

        if (tpe =:= info.gratour.common.Types.StringType)
          setNull(Types.VARCHAR)
        else if (tpe =:= info.gratour.common.Types.LocalDateType)
          setNull(Types.DATE)
        else if (tpe =:= info.gratour.common.Types.LocalTimeType)
          setNull(Types.TIME)
        else if (tpe =:= info.gratour.common.Types.OffsetDateTimeType)
          setNull(Types.TIMESTAMP_WITH_TIMEZONE)
        else if (tpe =:= info.gratour.common.Types.JBigDecimalType)
          setNull(Types.DECIMAL)
        else if (tpe =:= info.gratour.common.Types.JCharacterType)
          setNull(Types.CHAR)
        else if (tpe =:= info.gratour.common.Types.JBooleanType)
          setNull(Types.BOOLEAN)
        else if (tpe =:= info.gratour.common.Types.JIntegerType)
          setNull(Types.INTEGER)
        else if (tpe =:= info.gratour.common.Types.JShortType)
          setNull(Types.SMALLINT)
        else if (tpe =:= info.gratour.common.Types.JFloatType)
          setNull(Types.FLOAT)
        else if (tpe =:= info.gratour.common.Types.JDoubleType)
          setNull(Types.DOUBLE)
        else
          throw new ErrorWithCode(Errors.UNSUPPORTED_TYPE)
    }
  }

  def bind(values: Any*): Unit = macro StatementBinderMarcos.bind_impl


  def json(value: AnyRef): Unit = {
    if (value != null)
      setString(Consts.GSON.toJson(value))
    else
      setNull(Types.VARCHAR)
  }
}

object StatementBinderMarcos {


  def bind_impl(c: whitebox.Context)(values: c.Expr[Any]*): c.Expr[Unit] = {
    import c.universe._


    val cl = c.prefix
    val list = values.map(expr => {
      q"""
        $cl.set($expr);
       """
    }).toList

    c.Expr[Unit](q"{..$list}")
  }

}
