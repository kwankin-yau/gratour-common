package info.gratour.common

import org.junit.Test
import scala.reflect.runtime.universe._

class TypesTest {

  @Test
  def testType(): Unit = {
    if (Types.StringType =:= typeTag[java.lang.String].tpe) {
      println("string equals")
    } else
      println("string not equals")

    if (Types.BoolType =:= Types.JBooleanType) {
      println("boolean equals")
    } else
      println("boolean not equals")

    if (Types.IntType =:= Types.JIntegerType)
      println("int equals")
    else
      println("int not equals")

    if (Types.CharType =:= Types.JCharacterType)
      println("char equals")
    else
      println("char not equals")
  }

}
