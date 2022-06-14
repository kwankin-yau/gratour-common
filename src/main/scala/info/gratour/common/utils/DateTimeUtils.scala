package info.gratour.common.utils

import java.time.format.DateTimeFormatter
import java.time.{Clock, Duration, Instant, LocalDate, LocalDateTime, OffsetDateTime, ZoneId, ZoneOffset}
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference

object DateTimeUtils {

  final val DEFAULT_ZONE_OFFSET: ZoneOffset = defaultZoneOffset
  final val DEFAULT_ZONE_ID: ZoneId = ZoneId.systemDefault()

  final val ZONE_ID_Z: ZoneId = ZoneId.of("Z")
  final val ZONE_OFFSET_BEIJING: ZoneOffset = ZoneOffset.ofHours(8)


  final val CONVENIENT_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  final val CONVENIENT_DATETIME_FORMATTER_SHORT_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
  final val CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
  final val DATETIME_FORMATTER_WITH_ZONE1: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss x")
  final val DATETIME_FORMATTER_WITH_MILLIS_ZONE1: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS x")

  final val FILE_NAME_DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
  final val HTTP_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"))

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

  /**
   * Get ZoneId of specified zone offset(by hours).
   *
   * @return null if offset out of range.
   */
  def zoneIdOfOffsetHour(zoneOffsetHours: Int): ZoneId = {
    try {
      ZoneOffset.ofTotalSeconds(zoneOffsetHours * 60 * 60)
    } catch {
      case _: Exception =>
        null
    }
  }

  class CachedZoneOffset {
    val cachedZoneOffset: AtomicReference[ZoneOffset] = new AtomicReference[ZoneOffset](defaultZoneOffset)

    def recheck(): Unit = cachedZoneOffset.set(defaultZoneOffset)

    def millisToOffsetDateTimeString(epochMillis: Long): String =
      Instant.ofEpochMilli(epochMillis).atOffset(cachedZoneOffset.get()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    def parseDateTime(value: String): OffsetDateTime =
      if (value.contains('T'))
        OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
      else {
        if (value.contains('.'))
          LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS).atOffset(cachedZoneOffset.get())
        else
          LocalDateTime.parse(value, DateTimeUtils.CONVENIENT_DATETIME_FORMATTER).atOffset(cachedZoneOffset.get())
      }

    def stringToMillis(value: String): Long =
      parseDateTime(value).toInstant.toEpochMilli
  }

  def defaultZoneOffset: ZoneOffset = {
    val clock = Clock.systemDefaultZone()
    clock.getZone.getRules.getOffset(clock.instant())
  }

  def convenientDateTimeFormat(epochMillis: Long): String = Instant.ofEpochMilli(epochMillis).atOffset(DEFAULT_ZONE_OFFSET).format(CONVENIENT_DATETIME_FORMATTER)
  def dateTimeToFileName(dt: LocalDateTime): String = dt.format(FILE_NAME_DATETIME_FORMATTER)

  def offsetDateTimeNowString: String =
    OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

  def offsetDateTimeNowString(zoneId: ZoneId): String =
    OffsetDateTime.now().atZoneSameInstant(zoneId).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

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

  def millisToOffsetDateTimeString(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis).atOffset(defaultZoneOffset).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

  def offsetDateTimeStringToMillis(s: String): Long =
    OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant.toEpochMilli

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

  def stringToMillis(value: String): Long =
    parseDateTime(value).toInstant.toEpochMilli

  def tryStringToDate(s: String): LocalDate = {
    if (s != null) {
      try {
        LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE)
      } catch {
        case _: Throwable =>
          null
      }
    } else
      null
  }

  object BeijingConv {
    def millisToString(epochMillis: Long): String =
      Instant.ofEpochMilli(epochMillis).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER)

    def stringToMillis(s: String): Long =
      LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli

    def tryStringToMillis(s: String): java.lang.Long = {
      if (s != null) {
        try {
          LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli
        } catch {
          case _: Throwable =>
            null
        }
      } else
        null
    }

    def secondsToString(epochSeconds: Long): String =
      Instant.ofEpochSecond(epochSeconds).atOffset(ZONE_OFFSET_BEIJING).format(CONVENIENT_DATETIME_FORMATTER)

    def stringToSeconds(s: String): Long =
      LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli / 1000L

    def tryStringToSeconds(s: String): java.lang.Long = {
      if (s != null) {
        try {
          LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).toInstant(ZONE_OFFSET_BEIJING).toEpochMilli / 1000L
        } catch {
          case _: Throwable =>
            null
        }
      } else
        null
    }

    def stringToOffsetDateTime(s: String): OffsetDateTime =
      LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING)

    def tryStringToOffsetDateTime(s: String): OffsetDateTime = {
      if (s != null) {
        try {
          LocalDateTime.parse(s, CONVENIENT_DATETIME_FORMATTER).atOffset(ZONE_OFFSET_BEIJING)
        } catch {
          case _: Throwable =>
            null
        }
      } else
        null
    }



  }

}
