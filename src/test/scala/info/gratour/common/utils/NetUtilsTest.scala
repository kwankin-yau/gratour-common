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
  }
}
