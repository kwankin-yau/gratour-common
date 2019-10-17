package info.gratour.common.utils

import java.time.{ZoneId, ZoneOffset}

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


}
