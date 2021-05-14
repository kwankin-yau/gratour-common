package info.gratour.common.utils

import java.time.format.DateTimeFormatter
import java.time.{Clock, Duration, Instant, LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset}
import java.util.Locale

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

  def defaultZoneOffset: ZoneOffset = {
    val clock = Clock.systemDefaultZone()
    clock.getZone.getRules.getOffset(clock.instant())
  }

  val DEFAULT_ZONE_OFFSET: ZoneOffset = defaultZoneOffset
  val DEFAULT_ZONE_ID: ZoneId = ZoneId.systemDefault()

  val ZONE_ID_Z: ZoneId = ZoneId.of("Z")
  val ZONE_OFFSET_BEIJING: ZoneOffset = ZoneOffset.ofHours(8)


  val CONVENIENT_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val CONVENIENT_DATETIME_FORMATTER_SHORT_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
  val CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  val FILE_NAME_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
  val HTTP_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"))

  def convenientDateTimeFormat(epochMillis: Long): String = Instant.ofEpochMilli(epochMillis).atOffset(DEFAULT_ZONE_OFFSET).format(CONVENIENT_DATETIME_FORMATTER)
  def dateTimeToFileName(dt: LocalDateTime): String = dt.format(FILE_NAME_DATETIME_FORMATTER)

  /**
   * Produce format:
   * 39h 45m 47.045s
   * -39h -45m -47.045s
   *
   * @param duration
   * @return
   */
  def humanReadableDuration(duration: Duration): String =
    duration.toString.substring(2)
      .replaceAll("(\\d[HMS])(?!$)", "$1 ")
      .toLowerCase

  def epochMillisToBeijingOffsetDateTimeString(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis).atOffset(ZONE_OFFSET_BEIJING).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

  def parseDateTime(value: String): OffsetDateTime =
    if (value.contains('T'))
      OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    else {
      if (value.contains('.'))
        LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS).atOffset(DateTimeUtils.defaultZoneOffset)
      else
        LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER).atOffset(DateTimeUtils.defaultZoneOffset)
    }

  object BeijingConv {
    def millisToString(epochMillis: Long): String =
      Instant.ofEpochMilli(epochMillis).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER)

    def stringToMillis(s: String): Long =
      LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli

    def secondsToString(epochSeconds: Long): String =
      Instant.ofEpochSecond(epochSeconds).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER)

    def stringToSeconds(s: String): Long =
      LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli / 1000L
  }

}
