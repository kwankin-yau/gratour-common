package info.gratour.common.utils

import java.lang.{Boolean => JBoolean, Double => JDouble, Float => JFloat, Integer => JInteger, Long => JLong, Short => JShort}
import java.math.{BigDecimal => JDecimal}
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{LocalDate, LocalDateTime, OffsetDateTime}

object StringUtils {

  def arrayToString[T](arr: Array[T]): String = {
    arr.mkString("[", ",", "]")
  }

  implicit class StringImprovement(val s: String) {
    def nullOrEmpty: Boolean = s == null || s.isEmpty
  }

  def tryParseInt(s: String): JInteger = try
    s.toInt
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseZeroOneAsBool(value: String): JBoolean = value match {
    case "0" =>
      false
    case "1" =>
      true
    case _ =>
      null
  }

  def tryParseBool(value: String): JBoolean = try
    JBoolean.valueOf(value.toBoolean)
  catch {
    case e: IllegalArgumentException =>
      null
  }

  def tryParseShort(value: String): JShort = try java.lang.Short.parseShort(value)
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseLong(value: String): JLong = try java.lang.Long.parseLong(value)
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseDecimal(value: String): JDecimal = try new JDecimal(value)
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseFloat(value: String): JFloat = try JFloat.parseFloat(value)
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseDouble(value: String): JDouble = try JDouble.parseDouble(value)
  catch {
    case e: NumberFormatException =>
      null
  }

  def tryParseLocalDate(value: String): LocalDate = try LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
  catch {
    case e: DateTimeParseException =>
      null
  }

  def tryParseLocalDateTime(value: String): LocalDateTime = try LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
  catch {
    case e: DateTimeParseException =>
      null
  }

  def tryParseOffsetDateTime(value: String): OffsetDateTime = try OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  catch {
    case e: DateTimeParseException =>
      null
  }

  def arrayIndexOf(arr: Array[String], valueToFind: String): Int =
    arr.indexWhere(_ == valueToFind)

  private val DIGITS = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  /**
   * 字节数组转HEX字符串。
   *
   * @param bytes
   * 给定的字节数组。
   * @return HEX字符串。
   */
  def hex(bytes: Array[Byte]): String = {
    val str = new StringBuilder
    for (b <- bytes) {
      str.append(DIGITS((0xF0 & b) >>> 4))
      str.append(DIGITS(0x0F & b))
    }
    str.toString
  }

  def hex(bytes: Array[Byte], offset: Int, len: Int): String = {
    val str = new StringBuilder
    var i = offset
    while ( {
      i < offset + len
    }) {
      val b = bytes(i)
      str.append(DIGITS((0xF0 & b) >>> 4))
      str.append(DIGITS(0x0F & b))

      {
        i += 1;
        i - 1
      }
    }
    str.toString
  }

  def hex(s: String): Array[Byte] = {
    val l = s.length
    if (l % 2 != 0)
      throw new RuntimeException(s"Invalid hexadecimal string `$s`.")

    var r = new Array[Byte](s.length)
    var index = 0
    var h: Boolean = true
    var b: Byte = 0

    def put(digit: Int): Unit = {
      if (h) {
        b = (digit << 4).toByte
        h = false
      } else {
        b = (b | digit).toByte
        r(index) = b
        index = index + 1
        h = true
      }
    }

    s.foreach {
      case x if x >= '0' && x <= '9' =>
        put(x - '0')

      case x if x >= 'a' && x <= 'f' =>
        put(x - 'a' + 10)

      case x if x >= 'A' && x <= 'F' =>
        put(x - 'A' + 10)

      case _ =>
        throw new RuntimeException(s"Invalid hexadecimal string `$s`.")
    }

    r
  }
}
