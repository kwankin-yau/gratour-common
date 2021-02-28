package info.gratour.common

import java.time.{LocalDate, LocalDateTime, LocalTime, OffsetDateTime}

import info.gratour.common.lang.JavaByteArray
import info.gratour.common.types.EpochMillis

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

object Types {

  val BoolType: universe.Type = typeTag[Boolean].tpe
  val ByteType: universe.Type = typeTag[Byte].tpe
  val ShortType: universe.Type = typeTag[Short].tpe
  val IntType: universe.Type = typeTag[Int].tpe
  val LongType: universe.Type = typeTag[Long].tpe
  val CharType: universe.Type = typeTag[Char].tpe
  val StringType: universe.Type = typeTag[String].tpe
  val FloatType: universe.Type = typeTag[Float].tpe
  val DoubleType: universe.Type = typeTag[Double].tpe

  val JBooleanType: universe.Type = typeTag[java.lang.Boolean].tpe
  val JByteType: universe.Type = typeTag[java.lang.Byte].tpe
  val JShortType: universe.Type = typeTag[java.lang.Short].tpe
  val JIntegerType: universe.Type = typeTag[java.lang.Integer].tpe
  val JLongType: universe.Type = typeTag[java.lang.Long].tpe
  val JCharacterType: universe.Type = typeTag[java.lang.Character].tpe


  // note: JStringType =:= StringType
//  val JStringType: universe.Type = typeTag[java.lang.String].tpe

  val JFloatType: universe.Type = typeTag[java.lang.Float].tpe
  val JDoubleType: universe.Type = typeTag[java.lang.Double].tpe
  val JBigDecimalType: universe.Type = typeTag[java.math.BigDecimal].tpe
  val LocalDateType: universe.Type = typeTag[LocalDate].tpe
  val LocalTimeType: universe.Type = typeTag[LocalTime].tpe
  val LocalDateTimeType: universe.Type = typeTag[LocalDateTime].tpe
  val OffsetDateTimeType: universe.Type = typeTag[OffsetDateTime].tpe
  val EpochMillisType: universe.Type = typeTag[EpochMillis].tpe

  val InputStreamType: universe.Type = typeTag[java.io.InputStream].tpe

//  val ByteArrayType: universe.Type = typeTag[JavaByteArray.BYTE]
}
