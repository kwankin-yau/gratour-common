package info.gratour.common.db

import java.sql.Connection

import javax.sql.DataSource
import scalikejdbc.ConnectionPool

class ScalikeConnProvider(val connectionPool: ConnectionPool) extends ConnProvider {
  override def getConn: Connection = connectionPool.borrow()
  override def dataSource(): DataSource = connectionPool.dataSource
}
