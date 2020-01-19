package info.gratour.common.lang

import java.lang
import java.time.{LocalDate, LocalDateTime, OffsetDateTime}

object Reflections {

  val JBoolean: Class[lang.Boolean] = classOf[java.lang.Boolean]
  val JBooleanPrimitive: Class[lang.Boolean] = java.lang.Boolean.TYPE
  val JShort: Class[lang.Short] = classOf[java.lang.Short]
  val JShortPrimitive: Class[lang.Short] = java.lang.Short.TYPE
  val JInteger: Class[Integer] = classOf[java.lang.Integer]
  val JIntegerPrimitive: Class[Integer] = java.lang.Integer.TYPE
  val JLong: Class[lang.Long] = classOf[java.lang.Long]
  val JLongPrimitive: Class[lang.Long] = java.lang.Long.TYPE
  val JString: Class[String] = classOf[java.lang.String]
  val JFloat: Class[lang.Float] = classOf[java.lang.Float]
  val JFloatPrimitive: Class[lang.Float] = java.lang.Float.TYPE
  val JDouble: Class[lang.Double] = classOf[java.lang.Double]
  val JDoublePrimitive: Class[lang.Double] = java.lang.Double.TYPE
  val JBigDecimal: Class[java.math.BigDecimal] = classOf[java.math.BigDecimal]
  val JLocalDate: Class[LocalDate] = classOf[LocalDate]
  val JLocalDateTime: Class[LocalDateTime] = classOf[LocalDateTime]
  val JOffsetDateTime: Class[OffsetDateTime] = classOf[OffsetDateTime]
  val JByteArray: Class[_] = JavaByteArray.BYTE_ARRAY_CLASS
}
