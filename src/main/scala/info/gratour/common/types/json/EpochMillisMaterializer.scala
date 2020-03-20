package info.gratour.common.types.json

import java.lang.reflect.Type

import com.google.gson._
import info.gratour.common.types.EpochMillis

class EpochMillisMaterializer extends JsonSerializer[EpochMillis] with JsonDeserializer[EpochMillis] {

  override def serialize(src: EpochMillis, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
     new JsonPrimitive(src.millis)

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): EpochMillis =
    json.getAsJsonPrimitive.getAsLong

}
