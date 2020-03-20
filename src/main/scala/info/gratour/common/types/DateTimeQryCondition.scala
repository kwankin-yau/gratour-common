/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types

import java.time.Instant

import info.gratour.common.error.ErrorWithCode

trait DateTimeQryCondition {

  def validate: Boolean
  def check(): DateTimeQryCondition
  def toDateTimeExpr: String

}

case class InstantEqual(instant: Instant) extends DateTimeQryCondition {
  override def validate: Boolean = true

  override def check(): DateTimeQryCondition = this

  override def toDateTimeExpr: String = String.valueOf(instant.toEpochMilli)
}

object InstantEqual {
  def now(): InstantEqual = InstantEqual(Instant.now())
}

case class DateTimeRangeWithin31days(notBefore: EpochMillis, before: EpochMillis) extends DateTimeQryCondition {

  override def validate: Boolean = {
    if (notBefore == null)
      return false

    val end = if (before != null) before else EpochMillis.now()
    if (notBefore.toOffsetDateTimeZ.plusDays(31).plusMinutes(1).isBefore(end))
      return false

    true
  }

  override def check(): DateTimeQryCondition = {
    if (notBefore == null)
      throw ErrorWithCode.invalidParam("notBefore")

    val end = if (before != null) before else EpochMillis.now()
    if (notBefore.toOffsetDateTimeZ.plusDays(31).plusMinutes(1).isBefore(end))
      throw ErrorWithCode.invalidParam("before")

    this
  }

  override def toDateTimeExpr: String = {
    val str = new StringBuilder("[")
    str.append(notBefore.millis)

    if (before != null)
      str.append(",").append(before.millis).append(")")

    str.toString()
  }

  override def toString: String = toDateTimeExpr
}
