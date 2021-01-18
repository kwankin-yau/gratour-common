package info.gratour.common.types

class IncIndex {
  var index: Int = _

  def inc(): Int = {
    index += 1
    index
  }

  def inc(delta: Int): Int = {
    index += delta
    index
  }

}

object IncIndex {

  def apply(): IncIndex = new IncIndex()

  def apply(initIndex: Int): IncIndex = {
    val r = new IncIndex()
    r.index = initIndex
    r
  }
}
