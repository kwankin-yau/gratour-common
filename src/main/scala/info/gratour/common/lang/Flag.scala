package info.gratour.common.lang

class Flag {

  var value: Boolean = false

}

object Flag {

  def apply(value: Boolean): Flag = {
    val r = new Flag
    r.value = value
    r
  }

}
