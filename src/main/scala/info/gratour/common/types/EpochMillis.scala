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
import java.time.{Instant, LocalDate, LocalDateTime, OffsetDateTime, ZoneId, ZonedDateTime}

import info.gratour.common.utils.{DateTimeUtils, StringUtils}

case class EpochMillis(millis: Long) extends DateTimeQryCondition {

  def toOffsetDateTime: OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.DEFAULT_ZONE_ID)

  def toOffsetDateTime(zoneId: ZoneId): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId)

  def toOffsetDateTimeZ: OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.ZONE_ID_Z)

  def toLocalDateTimeBeijing: LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.ZONE_OFFSET_BEIJING)

  def toLocalDate: LocalDate =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.DEFAULT_ZONE_OFFSET).toLocalDate

  def toLocalDateTime: LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.DEFAULT_ZONE_ID)

  def toLocalDateBeijing: LocalDate =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.ZONE_OFFSET_BEIJING).toLocalDate

  def toZonedDateTimeZ: ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), DateTimeUtils.ZONE_ID_Z)

  def toInstant: Instant =
    Instant.ofEpochMilli(millis)

  def getYear: Int = toLocalDate.getYear
  def getMonth: Int = toLocalDate.getMonthValue
  def getMonthValue: Int = toLocalDate.getMonthValue
  def getDay: Int = toLocalDate.getDayOfMonth
  def getDayOfMonth: Int = toLocalDate.getDayOfMonth
  def getHour: Int = toLocalDateTime.getHour
  def getMinute: Int = toLocalDateTime.getMinute
  def getSecond: Int = toLocalDateTime.getSecond
  def getYMD: YMD = new YMD(toLocalDate)

  def toTimestamp: Timestamp = new Timestamp(millis)

  def toText: String =
    toOffsetDateTimeZ.format(DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS)

  def toConvenientDateTimeString: String =
    toOffsetDateTime(DateTimeUtils.DEFAULT_ZONE_OFFSET).format(DateTimeUtils.CONVENIENT_DATETIME_FORMATTER)

  override def toString: String =
    millis.toString


  override def equals(obj: Any): Boolean = {
    if (obj == null)
      false
    else {
      obj match {
        case epoch: EpochMillis =>
          millis == epoch.millis

        case odt: OffsetDateTime =>
          millis == odt.toInstant.toEpochMilli

        case zdt: ZonedDateTime =>
          millis == zdt.toInstant.toEpochMilli

        case date: java.util.Date =>
          millis == date.getTime

        case instant: Instant =>
          millis == instant.toEpochMilli

        case _ =>
          false
      }
    }
  }

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

  def startOfMonth(): EpochMillis = {
    val odt = OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1)
    new EpochMillis((odt.toInstant.toEpochMilli))
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
      epochMillis.toOffsetDateTime
    else
      null

  implicit def offsetDateTimeToEpochMillis(offsetDateTime: OffsetDateTime): EpochMillis =
    if (offsetDateTime != null)
      new EpochMillis(offsetDateTime.toInstant.toEpochMilli)
    else
      null

  implicit def epochMillisToString(epochMillis: EpochMillis): String =
    if (epochMillis != null)
      epochMillis.toConvenientDateTimeString
    else
      null

  implicit def stringToEpochMillis(s: String): EpochMillis =
    if (s != null && s.nonEmpty)
      new EpochMillis(StringUtils.tryParseOffsetDateTime(s).toInstant.toEpochMilli)
    else
      null
}

