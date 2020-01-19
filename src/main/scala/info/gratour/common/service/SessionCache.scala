package info.gratour.common.service

import java.time.Duration

import com.github.benmanes.caffeine.cache.{Cache, Caffeine}
import info.gratour.common.po.UserSession


class SessionCache[T <: UserSession](val ttl: Duration) {

  private val cache: Cache[String, T] = Caffeine.newBuilder()
    .expireAfterWrite(ttl)
    .build()

  def get(token: String): T = cache.getIfPresent(token)
  def set(session: T): Unit = cache.put(session.getToken, session)

}
