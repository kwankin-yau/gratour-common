/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.utils

import org.apache.commons.validator.routines.{DomainValidator, InetAddressValidator}
import org.xbill.DNS.{ARecord, Address, Lookup, SimpleResolver}

import java.net.{InetAddress, NetworkInterface, UnknownHostException}
import java.nio.charset.{Charset, StandardCharsets}
import java.security.MessageDigest
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap

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

  private final val resolverMap = new ConcurrentHashMap[String, SimpleResolver]()
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

  final val DomainValidatorAllowLocal: DomainValidator = DomainValidator.getInstance(true)

  def isValidDomain(domain: String): Boolean = {
    DomainValidatorAllowLocal.isValid(domain)
  }

  def isValidIpOrDomain(ipOrDomain: String): Boolean = {
    if (ipOrDomain == null || ipOrDomain.isEmpty)
      false
    else{
      if (ipOrDomain.charAt(0).isDigit) {
        var r = isValidIp(ipOrDomain)
        if (!r)
          r = isValidDomain(ipOrDomain)
        r
      } else {
        var r = DomainValidatorAllowLocal.isValid(ipOrDomain)
        if (!r)
          r = isValidIp(ipOrDomain)
        r
      }

    }
  }

  object HttpUtils {

    def calcBasicAuthorization(username: String, password: String, charset: Charset): String = {
      val bytes = (username + ":" + password).getBytes(charset)
      "Basic " + Base64.getEncoder.encodeToString(bytes)
    }

    def calcBasicAuthorization(username: String, password: String): String = calcBasicAuthorization(username, password, StandardCharsets.US_ASCII)

    def calcDigestAuthorization(username: String, password: String, realm: String, nonce: String, uri: String): String = {
      // HA1 = MD5(username:realm:password)
      // HA2 = MD5(method:digestURI)
      // response = MD5(HA1:nonce:HA2)

      val md5 = MessageDigest.getInstance("MD5")
      val ha1 = StringUtils.hex(md5.digest((username + ':' + realm + ':' + password).getBytes))

      md5.reset()
      val ha2 = StringUtils.hex(md5.digest(("MD5:" + uri).getBytes))

      md5.reset()
      val response = StringUtils.hex(md5.digest((ha1 + ":" + nonce + ":" + ha2).getBytes))

      // Authorization: Digest username="admin", realm="IP Camera(F3820)", nonce="f6a30073c0abd7372a8320e4ea6637bc", uri="rtsp://192.168.1.64:554/h264/ch1/main/av_stream", response="9e109a388b193cacc1bb0530b523af70"
      s"Digest username=\"${username}\", realm=\"${realm}\", nonce=\"${nonce}\", uri=\"${uri}\", response=\"${response}\""
    }
  }



}
