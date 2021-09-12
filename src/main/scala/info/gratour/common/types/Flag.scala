package info.gratour.common.types

class Flag {

  var value: Boolean = false

}

object Flag {

  def apply(value: Boolean): Flag = {
    val r = new Flag
    r.value = value
    r
  }

  def apply(): Flag = new Flag

}
