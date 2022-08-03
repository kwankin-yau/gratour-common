package info.gratour.common.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64

object CryptoUtils {

  def md5(data: Array[Byte]): Array[Byte] = {
    val md = MessageDigest.getInstance("MD5")
    md.digest(data)
  }

  /**
   * Calculate md5 of given data string (ASCII string only).
   *
   * @param data
   * @return
   */
  def md5(data: String): Array[Byte] = md5(data.getBytes(StandardCharsets.US_ASCII))

  def md5Hex(data: Array[Byte]): String = {
    val md = MessageDigest.getInstance("MD5")
    StringUtils.hex(md.digest(data))
  }

  /**
   * Calculate md5 of given data string (ASCII string only), return the hex encoded string for md5 digest bytes.
   *
   * @param data
   * @return
   */
  def md5Hex(data: String): String = md5Hex(data.getBytes(StandardCharsets.US_ASCII))

  def md5Base64(data: Array[Byte]): String = {
    val md = MessageDigest.getInstance("MD5")
    Base64.getEncoder.encodeToString(md.digest(data))
  }

  /**
   * Calculate md5 of given data string (ASCII string only), return the base64 encoded string for md5 digest bytes.
   *
   * @param data
   * @return
   */
  def md5Base64(data: String): String = md5Base64(data.getBytes(StandardCharsets.US_ASCII))
}
