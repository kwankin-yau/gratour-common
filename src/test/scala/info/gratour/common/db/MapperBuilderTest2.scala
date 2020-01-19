package info.gratour.common.db

import org.junit.{Ignore, Test}
import scalikejdbc._


class MapperBuilderTest2 {
  Class.forName("org.postgresql.ds.PGSimpleDataSource")
  ConnectionPool.singleton("jdbc:postgresql://localhost:5432/gmsz", "gmbj", "gmbj")

  val sql = "SELECT f_user_name, f_passwd_seed, f_passwd_md5_hex, f_sub_area_id FROM t_user WHERE f_user_name = ?"

  val mapper: WrappedResultSet => UserEntry = MapperBuilder.build(sql, classOf[UserEntry])

  def test(): Unit = {
    implicit val session = AutoSession
    val list = SQL(sql)
      .bind("admin")
      .map(mapper)
      .list()
      .apply()

    println(list)
  }

}

object MapperBuilderTest2 {
  def main(args: Array[String]): Unit = {
    new MapperBuilderTest2().test()
  }
}
