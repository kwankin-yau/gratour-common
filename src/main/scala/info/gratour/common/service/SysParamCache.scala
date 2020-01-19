package info.gratour.common.service

import java.util.concurrent.TimeUnit

import com.github.benmanes.caffeine.cache.{Caffeine, LoadingCache}
import info.gratour.common.dao.SysParamDao
import info.gratour.common.db.{ConnProvider, DbSupport}
import info.gratour.common.po.{ScopedSysParamName, SysParamInfo}

trait SysParamCacheService {

  def get(scope: String, paramName: String): SysParamInfo

  def put(sysParamInfo: SysParamInfo): Unit

  def put(scope: String, paramName: String, paramValue: String): SysParamInfo
}

class SysParamCache(val connProvider: ConnProvider, val cacheSize: Int, val ttlSeconds: Int) extends SysParamCacheService with DbSupport {



  private val cache: LoadingCache[String, SysParamInfo] = Caffeine.newBuilder()
    .maximumSize(cacheSize)
    .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
    .build(qualifiedName => {
      inSession(implicit session => {
        val name = ScopedSysParamName.parse(qualifiedName)
        SysParamDao.query(name.scope, name.paramName)
      })
    })

  override def get(scope: String, paramName: String): SysParamInfo = cache.get(SysParamInfo.qualifyParamName(scope, paramName))

  override def put(sysParamInfo: SysParamInfo): Unit = cache.put(SysParamInfo.qualifyParamName(sysParamInfo.scope, sysParamInfo.paramName), sysParamInfo)

  override def put(scope: String, paramName: String, paramValue: String): SysParamInfo = {
    val r = SysParamInfo(scope, paramName, paramValue)
    cache.put(r.qualifiedParamName(), r)
    r
  }
}

object SysParamCache {
  def apply(connProvider: ConnProvider, cacheSize: Int, ttlSeconds: Int): SysParamCache = new SysParamCache(connProvider, cacheSize, ttlSeconds)
}
