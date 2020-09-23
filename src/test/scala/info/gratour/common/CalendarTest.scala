/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common

import java.util.{Calendar, TimeZone}

import org.junit.Test

class CalendarTest {

  @Test
  def test(): Unit = {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("+08:00"))

//    cal.setTimeInMillis(System.currentTimeMillis())

    val h = cal.get(Calendar.HOUR)
    println(s"h = $h")
  }
}
