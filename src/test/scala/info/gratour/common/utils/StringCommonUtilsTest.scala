package info.gratour.common.utils

import org.junit.{Assert, Test}

class StringCommonUtilsTest {

  @Test
  def testArrayToString(): Unit = {
    val strArr = Array("1", "a")
    Assert.assertEquals(StringUtils.arrayToString(strArr), "[1,a]")

    val intArr = Array(1, 3)
    Assert.assertEquals(StringUtils.arrayToString(intArr), "[1,3]")
  }
}
