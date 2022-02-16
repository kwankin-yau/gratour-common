/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
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
