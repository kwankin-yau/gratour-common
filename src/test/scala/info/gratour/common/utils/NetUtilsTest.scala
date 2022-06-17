/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.utils

object NetUtilsTest {

  def main(args: Array[String]): Unit = {
    val addr = NetUtils.resolvePublicIp("gratour.info", "202.96.134.133")
    println(s"resolver addr: ${addr.getHostAddress}")
    val addr2 = NetUtils.resolvePublicIp("gratour.info", "202.96.134.133")
    println(s"resolver addr: ${addr2.getHostAddress}")

    // Authorization: Digest username="admin", realm="IP Camera(F3820)", nonce="53c33f2ca25ae2a17df0da1625a06901", uri="rtsp://192.168.1.64:554/h264/ch1/main/av_stream", response="e7bafcb005bb3c386f4febcc8828007b"
    val digest = NetUtils.HttpUtils.calcDigestAuthorization(
      "admin",
      "PTKptk12345678",
      "IP Camera(F3820)",
      "53c33f2ca25ae2a17df0da1625a06901",
      "DESCRIBE",
      "rtsp://192.168.1.64:554/h264/ch1/main/av_stream"
    )
    println(digest)
  }
}
