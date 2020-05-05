/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types

import java.sql.Timestamp
import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDateTime, OffsetDateTime, ZonedDateTime}

import info.gratour.common.Consts

case class EpochMillis(millis: Long) extends DateTimeQryCondition {

  def toOffsetDateTimeZ: OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), Consts.ZONE_ID_Z)

  def toLocalDateTimeBeijing: LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), Consts.ZONE_OFFSET_BEIJING)

  def toZonedDateTimeZ: ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), Consts.ZONE_ID_Z)

  def toInstant: Instant =
    Instant.ofEpochMilli(millis)

  def toTimestamp: Timestamp = new Timestamp(millis)

  def toText: String =
    toOffsetDateTimeZ.format(Consts.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS)

  override def toString: String =
    millis.toString

  override def validate: Boolean = true

  override def check(): DateTimeQryCondition = this

  override def toDateTimeExpr: String = String.valueOf(millis)
}

object EpochMillis {
  def now(): EpochMillis = new EpochMillis(System.currentTimeMillis())
  def startOfToday(): EpochMillis = {
    val odt = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
    new EpochMillis(odt.toInstant.toEpochMilli)
  }

  def apply(offsetDateTime: OffsetDateTime): EpochMillis =
    if (offsetDateTime != null)
      new EpochMillis(offsetDateTime.toInstant.toEpochMilli)
    else
      null

  implicit def epochMillisToLong(epochMillis: EpochMillis): Long =
    if (epochMillis != null)
      epochMillis.millis
    else
      0L

  implicit def longToEpochMillis(v: Long): EpochMillis = new EpochMillis(v)

  implicit def epochMillisToOffsetDateTime(epochMillis: EpochMillis): OffsetDateTime =
    if (epochMillis != null)
      epochMillis.toOffsetDateTimeZ
    else
      null

  implicit def offsetDateTimeToEpochMillis(offsetDateTime: OffsetDateTime): EpochMillis =
    if (offsetDateTime != null)
      new EpochMillis(offsetDateTime.toInstant.toEpochMilli)
    else
      null
}

