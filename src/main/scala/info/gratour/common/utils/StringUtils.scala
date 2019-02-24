package info.gratour.common.utils

object StringUtils {

  def arrayToString[T](arr: Array[T]): String = {
    arr.mkString("[", ",", "]")
  }

  implicit class StringImprovement(val s: String) {
    def nullOrEmpty: Boolean = s == null || s.isEmpty
  }


}
