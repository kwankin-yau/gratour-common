/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common

import java.util.{Locale, ResourceBundle}

object CommonMessages {

  private val BUNDLE: ResourceBundle = ResourceBundle.getBundle("info.gratour.common.messages", Locale.getDefault)

  val UNKNOWN: String = BUNDLE.getString("msg.unknown")
}
