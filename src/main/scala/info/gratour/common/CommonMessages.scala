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

  private final val BUNDLE: ResourceBundle = ResourceBundle.getBundle("info.gratour.common.messages", Locale.getDefault)

  def resourceBundle(locale: Locale): ResourceBundle = ResourceBundle.getBundle("info.gratour.common.messages", locale)

  final val UNKNOWN: String = BUNDLE.getString("msg.unknown")
  def unknown(locale: Locale): String = resourceBundle(locale).getString("msg.unknown")
}
