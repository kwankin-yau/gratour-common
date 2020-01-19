package info.gratour.common.rest

import java.lang.reflect.{ParameterizedType, Type}

import com.google.gson._

class OptionMaterializer extends JsonSerializer[Option[_]] with JsonDeserializer[Option[_]] {


  override def serialize(src: Option[_], typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    src match {
      case Some(value) => context.serialize(value)
      case None => JsonNull.INSTANCE
    }
  }

  /**
   * Actually, `null` can not be deserialized !
   *
   * @param json
   * @param typeOfT
   * @param context
   * @return
   */
  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Option[_] = {
    if (json == null)
      None
    else
      Option(context.deserialize(json, getFirstParameterizedTypeArgument(typeOfT)))
  }

  def getFirstParameterizedTypeArgument(valueType: Type): Type = {
    val parameterizedType = valueType.asInstanceOf[ParameterizedType]
    parameterizedType.getActualTypeArguments()(0)
  }
}
