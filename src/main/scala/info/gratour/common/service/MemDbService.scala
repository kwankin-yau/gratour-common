/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.service

import java.util
import java.util.concurrent.TimeUnit

trait MemDbService extends AutoCloseable {

  def keys(pattern: String): java.lang.Iterable[String]
  def keyStream(pattern: String): java.util.stream.Stream[String]

  def exists(key: String): Boolean

  def get(key: String): String
  def jsonGet[T](key: String)(implicit m: Manifest[T]): T
  def binaryGet(key: String): Array[Byte]

  def set(key: String, value: String): Unit
  def set(key: String, value: String, ttlSeconds: Int): Unit
  def jsonSet[T](key: String, value: T): Unit
  def jsonSet[T](key: String, value: T, ttlSeconds: Int): Unit
  def binarySet(key: String, value: Array[Byte]): Unit
  def binarySet(key: String, value: Array[Byte], ttlSeconds: Int): Unit

  def del(key: String): Unit

  def getLock(key: String): SimpleLock

  def sAdd(setName: String, member: String): Unit
  def sCount(setName: String): Int
  def sIsMember(setName: String, member: String): Boolean
  def sMembers(setName: String, result: util.Collection[String]): Unit
  def sMove(sourceSetName: String, destSetName: String, member: String): Boolean
  def sRemove(setName: String, member: String): Boolean
}

trait SimpleLock {
  def lock(): Unit
  def lock(leaseTime: Long, unit: TimeUnit): Unit
  def tryLock(waitTime: Long, unit: TimeUnit): Boolean
  def tryLock(waitTime: Long, leaseTime: Long, unit: TimeUnit): Boolean
  def unlock(): Unit
}
