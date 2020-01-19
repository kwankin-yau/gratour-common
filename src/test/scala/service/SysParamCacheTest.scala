package service

import java.sql.Connection

import info.gratour.common.db.ConnProvider
import info.gratour.common.service.{SysParamCache, SysParamCacheService}
import info.gratour.common.utils.LoanPattern._
import org.h2.jdbcx.JdbcDataSource
import org.junit.Test

class SysParamCacheTest {

//  private val logger = Logger[SysParamCacheTest]

  val _dataSource: JdbcDataSource = {
    val ds = new JdbcDataSource()
    ds.setURL("jdbc:h2:mem:test")
    ds.setUser("sa")
    ds.setPassword("")
    ds
  }

  val connProvider: ConnProvider = () => _dataSource

  // keep connection so contents in memory database will be retained
  val keepConn: Connection = {
    val conn = connProvider.getConn
    using(conn.createStatement()) { st =>
      st.execute("CREATE DOMAIN d_sys_param_scope AS VARCHAR(200);")

      st.execute("CREATE DOMAIN d_sys_param_name AS VARCHAR(200);")

      st.execute(
        """
CREATE TABLE t_sys_param (
    f_scope     d_sys_param_scope     NOT NULL ,
    f_param_name    d_sys_param_name NOT NULL ,
    f_param_value TEXT      ,

    PRIMARY KEY (f_scope, f_param_name)
);
          """)

      st.execute("INSERT INTO t_sys_param (f_scope, f_param_name, f_param_value) VALUES ('*', 'inventory.latest_import_date', NULL);")
    }

    conn
  }

  @Test
  def test(): Unit = {
    val cache: SysParamCacheService = SysParamCache(connProvider, 10, 20)
    cache.get("*", "inventory.latest_import_date")
    Thread.sleep(2000)
    cache.get("*", "inventory.latest_import_date")
  }

}
