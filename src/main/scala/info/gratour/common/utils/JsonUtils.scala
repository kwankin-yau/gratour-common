/** *****************************************************************************
 * Copyright (c) 2019, 2022 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.utils

import com.google.gson.JsonObject

object JsonUtils {

  /**
   * Merge "source" into "target". If fields have equal name, merge them recursively.
   *
   * @return the merged object (target).
   */
  def deepMerge(target: JsonObject, source: JsonObject): Unit = {
    source.entrySet().forEach(e => {
      val key = e.getKey
      val value = e.getValue

      if (!target.has(key)) {
        target.add(key, value)
      } else {
        value match {
          case obj: JsonObject =>
            deepMerge(obj, target.getAsJsonObject(key))

          case _ =>
            target.add(key, value)
        }
      }
    })
  }

}
