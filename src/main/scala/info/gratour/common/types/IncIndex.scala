/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
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
