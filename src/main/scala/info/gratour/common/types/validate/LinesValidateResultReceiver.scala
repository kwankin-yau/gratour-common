/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.types.validate

import info.gratour.common.error.Errors

import java.util

class LinesValidateResultReceiver extends AbstractValidateResultReceiver {
  private val lines = new util.ArrayList[String]()

  override def invalidField(fieldName: String): Unit = {
    val msg = Errors.errorMessageFormat(Errors.INVALID_PARAM, fieldName)
    lines.add(msg)
    error = true
  }

  def getLines: util.List[String] = lines

  def getLinesString: String = {
    val sb = new StringBuilder
    for (i <- 0 until lines.size()) {
      val ln = lines.get(i)
      if (sb.nonEmpty)
        sb.append("\n")

      sb.append(ln)
    }

    sb.toString()
  }

}
