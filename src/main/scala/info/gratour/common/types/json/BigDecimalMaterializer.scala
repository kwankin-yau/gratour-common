/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.types.json

import java.lang.reflect.Type
import java.math.RoundingMode
import java.math.BigDecimal
import com.google.gson.{JsonDeserializationContext, JsonDeserializer, JsonElement, JsonPrimitive, JsonSerializationContext, JsonSerializer}

class BigDecimalMaterializer(scale: Int, roundingMode: RoundingMode) extends JsonSerializer[BigDecimal] with JsonDeserializer[BigDecimal] {
  override def serialize(src: BigDecimal, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    new JsonPrimitive(src.setScale(scale, roundingMode).toPlainString)
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BigDecimal = {
    val s = json.getAsJsonPrimitive.getAsString
    new BigDecimal(s).setScale(scale, roundingMode)
  }
}
