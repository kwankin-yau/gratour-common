package info.gratour.common.utils

import java.lang.{Boolean => JBoolean, Double => JDouble, Float => JFloat, Integer => JInteger, Long => JLong, Short => JShort}
import java.math.{BigDecimal => JDecimal}
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{Instant, LocalDate, LocalDateTime, OffsetDateTime}

import info.gratour.common.Consts


object StringUtils {

  def isNullOrEmpty(s: String): Boolean = s == null || s.isEmpty

  def arrayToString[T](arr: Array[T]): String = {
    if (arr != null)
      arr.mkString("[", ",", "]")
    else
      null
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

  val FILE_NAME_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

  def epochMilliToFileNamePart(epochMilli: Long): String = {
    val odt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), Consts.ZONE_ID_Z)
    odt.format(FILE_NAME_DATE_TIME_FORMATTER)
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
    if (bytes == null)
      return null
    else if (bytes.isEmpty)
      return "";

    val str = new StringBuilder
    for (b <- bytes) {
      str.append(DIGITS((0xF0 & b) >>> 4))
      str.append(DIGITS(0x0F & b))
    }
    str.toString
  }

  def hex(bytes: Array[Byte], offset: Int, len: Int): String = {
    if (bytes == null)
      return null

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
    if (s == null)
      return null

    val l = s.length
    if (l % 2 != 0)
      throw new RuntimeException(s"Invalid hexadecimal string `$s`.")

    val r = new Array[Byte](l / 2)
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

  def strLen(bytes: Array[Byte]): Int = {
    for (i <- bytes.indices) {
      val b = bytes(i)
      if (b == 0)
        return i;
    }

    bytes.length
  }

  def strLen(bytes: Array[Byte], maxLen: Int): Int = {
    val m =
      if (maxLen > bytes.length)
        bytes.length
      else
        maxLen

    for (i <- 0 until m) {
      val b = bytes(i)
      if (b == 0)
        return i;
    }

    m
  }

  def cStr(bytes: Array[Byte], maxLen: Int): String = {
    if (bytes == null)
      return null

    val l = strLen(bytes, maxLen)
    new String(bytes, 0, l)
  }

  def cStr(bytes: Array[Byte]): String =
    if (bytes == null)
      null
    else
      cStr(bytes, bytes.length)


  /**
   * 移除字符串的前导'0'字符
   *
   * @param value
   * 所要处理的字符串
   * @return 处理后的字符串
   */
  def removeLeadingZero(value: String): String = {
    var beginIndex = 0
    var i = 0
    val count = value.codePointCount(0, value.length)
    while (i < count && beginIndex == 0) {
      val cp = value.codePointAt(i)
      if (cp != '0')
        beginIndex = i
      else
        i += 1
    }

    if (beginIndex == 0) value
    else value.substring(beginIndex)
  }

  def intToHex(value: Int, minLen: Int): String = {
    val r = Integer.toHexString(value)
    if (r.length < minLen)
      org.apache.commons.lang3.StringUtils.leftPad(r, minLen, '0')
    else
      r
  }
}
