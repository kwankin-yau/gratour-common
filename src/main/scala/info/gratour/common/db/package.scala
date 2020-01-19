package info.gratour.common

import scalikejdbc.WrappedResultSet

package object db {

  type ScalikeMapper[T <: AnyRef] = WrappedResultSet => T
}
