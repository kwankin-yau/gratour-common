package info.gratour.common.dao

import info.gratour.common.db.schema.FieldDataType
import info.gratour.common.db.sqlgen.{ColumnKind, TableSchemaBuilder}
import info.gratour.common.db.{QueryParams, QueryParamsBuilder, QueryParamsSpec, QueryResult, ScalikeMapper}
import info.gratour.common.po.SysParamInfo
import scalikejdbc.{DBSession, WrappedResultSet}

object SysParamDao {

  private val TABLE_SCHEMA = TableSchemaBuilder("t_sys_param")
    .column("f_scope", FieldDataType.TEXT, ColumnKind.PRIMARY_KEY)
    .column("f_param_name", FieldDataType.TEXT, ColumnKind.PRIMARY_KEY)
    .column("f_param_value", FieldDataType.TEXT)
    .build()

  private val CLASS_BIND = TABLE_SCHEMA.upsertBuilderClassBind(classOf[SysParamInfo])
  private val INSERT = CLASS_BIND.upsertBuilder().buildInsert
  private val UPDATE = CLASS_BIND.upsertBuilder().buildUpdate

  private val SELECT =
    """
      SELECT f_scope, f_param_name, f_param_value FROM t_sys_param
    """

  private val MAPPER = new ScalikeMapper[SysParamInfo] {
    override def apply(v1: WrappedResultSet): SysParamInfo = SysParamInfo(v1.string(1), v1.string(2), v1.stringOpt(3).orNull)
  }

  private val QUERY_SPEC: QueryParamsSpec[SysParamInfo] = TABLE_SCHEMA.queryParamsBuilder(SELECT, classOf[SysParamInfo], MAPPER)
    .conditionsFromSelect(null)
    .orderByAllParams()
    .paginationOpt()
    .build()

  val SCOPE_ALL = "*"

  object Conditions {
    val SCOPE = "scope"
    val PARAM_NAME = "paramName"
  }

  def query(scope: String, paramName: String)(implicit session: DBSession): SysParamInfo = {
    val queryParams = QueryParamsBuilder()
      .condition(Conditions.SCOPE, scope)
      .condition(Conditions.PARAM_NAME, paramName)
      .build()
    QUERY_SPEC.check(queryParams).query().first.orNull
  }

  def query(queryParams: QueryParams)(implicit session: DBSession): QueryResult[SysParamInfo] =
    QUERY_SPEC.check(queryParams).query()

  def insert(sysParamInfo: SysParamInfo)(implicit session: DBSession): SysParamInfo = {
    INSERT.execute(sysParamInfo)
    sysParamInfo
  }

  def update(sysParamInfo: SysParamInfo)(implicit session: DBSession): SysParamInfo = {
    if (UPDATE.execute(sysParamInfo) > 0)
      sysParamInfo
    else
      null
  }

  def delete(sysParamInfo: SysParamInfo)(implicit session: DBSession): Boolean =
    TABLE_SCHEMA.deleteEntry(sysParamInfo)

}
