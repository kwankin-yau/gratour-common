package info.gratour.common.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object CryptoUtils {

  def md5Hex(data: Array[Byte]): String = {
    val md = MessageDigest.getInstance("MD5")
    StringUtils.hex(md.digest(data))
  }

  // only support ASCII data
  def md5Hex(data: String): String = md5Hex(data.getBytes(StandardCharsets.US_ASCII))

}
