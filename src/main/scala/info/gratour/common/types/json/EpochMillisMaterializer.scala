/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types.json

import java.lang.reflect.Type

import com.google.gson._
import info.gratour.common.types.EpochMillis
import info.gratour.common.utils.DateTimeUtils

class EpochMillisMaterializer extends JsonSerializer[EpochMillis] with JsonDeserializer[EpochMillis] {

  override def serialize(src: EpochMillis, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
    new JsonPrimitive(src.millis)

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): EpochMillis =
    json.getAsJsonPrimitive.getAsLong

}

class EpochMillisConventionDateTimeMaterializer extends JsonSerializer[EpochMillis] with JsonDeserializer[EpochMillis] {
  override def serialize(src: EpochMillis, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    new JsonPrimitive(src.toConvenientDateTimeString)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): EpochMillis = {
    val s = json.getAsJsonPrimitive.getAsString
    if (s != null)
      DateTimeUtils.parseDateTime(s)
    else
      null
  }
}
