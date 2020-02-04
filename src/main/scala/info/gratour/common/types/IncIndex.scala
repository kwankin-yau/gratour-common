package info.gratour.common.types

class IncIndex {
  var index: Int = _

  def inc(): Int = {
    index += 1
    index
  }


}

object IncIndex {

  def apply(): IncIndex = new IncIndex()
}
