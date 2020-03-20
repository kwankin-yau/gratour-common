package info.gratour.common.utils

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset}

import info.gratour.common.types.EpochMillis

object DateTimeUtils {

  /**
    * Get ZoneId of specified id string.
    *
    * @return null if zoneId not found or invalid.
    */
  def zoneIdOf(zoneId: String): ZoneId = {
    if (zoneId == null || zoneId.isEmpty)
      return null

    try {
      ZoneId.of(zoneId)
    } catch {
      case _: Exception =>
        null
    }
  }

  /**
    * Get ZoneId of specified zone offset(by minutes).
    *
    * @return null if offset out of range.
    */
  def zoneIdOfOffset(zoneOffsetMinutes: Int): ZoneId = {
    try {
      ZoneOffset.ofTotalSeconds(zoneOffsetMinutes * 60)
    } catch {
      case _: Exception =>
        null
    }
  }

  val CONVENIENT_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  val FILE_NAME_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")

  def dateTimeToFileName(dt: LocalDateTime): String = dt.format(FILE_NAME_DATETIME_FORMATTER)
}
