/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.utils

import java.net.{InetAddress, NetworkInterface, UnknownHostException}
import java.util.concurrent.ConcurrentHashMap
import org.apache.commons.validator.routines.InetAddressValidator
import org.xbill.DNS.{ARecord, Address, Lookup, Options, Record, SimpleResolver, Type}

object NetUtils {

  /**
   * Determines the IP address of a host
   *
   * @param host The hostname to look up
   * @return The first matching IP address
   * @exception UnknownHostException The hostname does not have any addresses
   */
  def resolvePublicIp(host: String): InetAddress =
    Address.getByName(host)

  private val resolverMap = new ConcurrentHashMap[String, SimpleResolver]()
  def shutdownDnsSelector(): Unit = {
    org.xbill.DNS.NioClient.close()
  }


  /**
   * Determines the IP address of a host using specified dns server
   *
   * @param host  The hostname to look up
   * @param dns the dns server
   * @return The first matching IP address
   * @exception UnknownHostException The hostname does not have any addresses
   */
  def resolvePublicIp(host: String, dns: String): InetAddress = {
    if (dns == null)
      return Address.getByName(host)

    var resolver = resolverMap.get(dns)
    if (resolver == null) {
      resolver = new SimpleResolver(dns)
      val old = resolverMap.putIfAbsent(dns, resolver)
      if (old != null)
        resolver = old
    }

    val lookup = new Lookup(host)
    lookup.setResolver(resolver)

    val records = lookup.run()
    if (records != null && records.nonEmpty) {
      records(0).asInstanceOf[ARecord].getAddress
    } else
      throw new UnknownHostException(host)
  }

  def isValidPublicIp(ip: String): Boolean = {
    var addr: InetAddress = null

    try {
      addr = InetAddress.getByName(ip)
    } catch {
      case _: UnknownHostException =>
        return false
    }

    !(addr.isSiteLocalAddress || addr.isAnyLocalAddress || addr.isLinkLocalAddress || addr.isLoopbackAddress || addr.isMulticastAddress)
  }

  def isValidIpV4Addr(ipv4: String): Boolean =
    InetAddressValidator.getInstance().isValidInet4Address(ipv4)

  def isValidIpV6Addr(ipv6: String): Boolean =
    InetAddressValidator.getInstance().isValidInet6Address(ipv6)

  def isValidIp(ip: String): Boolean =
    InetAddressValidator.getInstance().isValid(ip)

  def isLocalAddr(addr: String): Boolean = {
    try {
      // Check if the address is a valid special local or loop back
      val inetAddr = InetAddress.getByName(addr)
      if (inetAddr.isAnyLocalAddress || inetAddr.isLoopbackAddress)
        return true

      // Check if the address is defined on any interface
      NetworkInterface.getByInetAddress(inetAddr) != null
    } catch {
      case _: Throwable =>
        false
    }
  }

  def isValidPortNum(port: Int): Boolean = port > 0 && port < 65536
}
