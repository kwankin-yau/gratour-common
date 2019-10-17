package info.gratour.common.utils

object StringUtils {

  def arrayToString[T](arr: Array[T]): String = {
    arr.mkString("[", ",", "]")
  }

  implicit class StringImprovement(val s: String) {
    def nullOrEmpty: Boolean = s == null || s.isEmpty
  }

  def tryParseInt(s: String): Integer = try
    s.toInt
  catch {
    case e: NumberFormatException =>
      null
  }

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
        i += 1; i - 1
      }
    }
    str.toString
  }


}
