/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common

import info.gratour.common.error.Errors
import info.gratour.common.types.rest.RawReply
import org.junit.Test

class ResourceBundleTest {

  @Test
  def test(): Unit = {
    println(Errors.errorMessage(Errors.OK))
    val reply = new RawReply(Errors.OK)
    println(reply.getMessage)
  }
}
