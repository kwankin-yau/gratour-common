package info.gratour.common.rest

import java.lang.reflect.Type
import java.time._
import java.time.format.DateTimeFormatter

import com.google.gson._

class LocalDateMaterializer extends JsonSerializer[LocalDate] with JsonDeserializer[LocalDate] {

  override def serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val text = DateTimeFormatter.ISO_LOCAL_DATE.format(src)
    new JsonPrimitive(text)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate = {
    val text = json.getAsString
    if (text.isEmpty)
      null
    else
      LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE)
  }
}

class LocalTimeMaterializer extends JsonSerializer[LocalTime] with JsonDeserializer[LocalTime] {
  override def serialize(src: LocalTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val text = DateTimeFormatter.ISO_LOCAL_TIME.format(src)
    new JsonPrimitive(text)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime = {
    val text = json.getAsString
    if (text.isEmpty)
      null
    else
      LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME)
  }
}


class LocalDateTimeMaterializer extends JsonSerializer[LocalDateTime] with JsonDeserializer[LocalDateTime] {
  override def serialize(src: LocalDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val text = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(src)
    new JsonPrimitive(text)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime = {
    val text = json.getAsString
    if (text.isEmpty)
      null
    else
      LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
  }
}

class OffsetDateTimeMaterializer extends JsonSerializer[OffsetDateTime] with JsonDeserializer[OffsetDateTime] {
  override def serialize(src: OffsetDateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val text = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(src)
    new JsonPrimitive(text)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): OffsetDateTime = {
    val text = json.getAsString
    if (text.isEmpty)
      null
    else
      OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }
}

class ZoneIdMaterializer extends JsonSerializer[ZoneId] with JsonDeserializer[ZoneId]{
  override def serialize(src: ZoneId, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    if (src == null)
      return JsonNull.INSTANCE

    val text = src.getId
    new JsonPrimitive(text)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ZoneId = {
    val text = json.getAsString
    if (text == null || text.isEmpty)
      null
    else
      ZoneId.of(text)
  }
}
