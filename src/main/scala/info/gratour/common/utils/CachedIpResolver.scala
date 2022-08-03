/** *****************************************************************************
 * Copyright (c) 2019, 2022 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.utils

import info.gratour.common.utils.CachedIpResolver.CachedIp

import java.util.concurrent.atomic.AtomicReference

class CachedIpResolver(ttl: Int, host: String, dns: String) {

  private final val IsIp = NetUtils.isValidIp(host)
  private final val CachedIpRef: AtomicReference[CachedIp] = new AtomicReference[CachedIp]()

  def resolve: String =
    if (IsIp) host
    else {
      var cached = CachedIpRef.get()
      if (cached != null) {
        if (!cached.isExpired(ttl))
          return cached.ip
      }

      val addr =
        if (dns != null)
          NetUtils.resolvePublicIp(host, dns)
        else
          NetUtils.resolvePublicIp(host)

      val ip = addr.getHostAddress

      cached = CachedIp(ip, System.currentTimeMillis())
      CachedIpRef.set(cached)

      ip
    }

}


object CachedIpResolver {
  private case class CachedIp(ip: String, resolveTime: Long) {

    def isExpired(ttl: Int): Boolean =
      System.currentTimeMillis() - resolveTime > ttl * 1000

  }
}
