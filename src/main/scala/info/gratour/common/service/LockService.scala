/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.service

import java.time.format.DateTimeFormatter
import java.time.{Instant, OffsetDateTime}

import info.gratour.common.Consts


/**
 *
 * @param lockId id of lock
 * @param ttl    Time-To-Live in seconds
 */
case class Lockable(lockId: String, ttl: Int)

/**
 * 锁信息
 */
case class Lock(lockService: LockService, lockable: Lockable, lockerName: String, lockTime: Long) {

  /**
   * 锁定信息，一般用于记录锁定者、锁定时间。<br/>
   * 约定格式：<锁定者名称>@<锁定时间>，如：locker1@2014-07-28 11:15:00
   *
   * @return
   */
  def lockInfo: String = LockService.lockInfo(lockerName, lockTime)

  def timeoutAt: Long = lockTime + lockable.ttl * 1000L

  def isTimedOut: Boolean = System.currentTimeMillis() > timeoutAt

  def unlock: LockService.UnlockResult =
    lockService.unlock(lockable.lockId, lockInfo)

  /**
   * Keep and return a new lock
   *
   * @return new lock
   */
  def keepLock: Lock =
    lockService.keepLock(this)
}


/**
 * 全局锁，用于服务器间同步。该锁定具有超时特性，即一个锁如果不解锁，则超时时间到了锁定也会消失。
 */
trait LockService extends AutoCloseable {

  /**
   * 实施锁定。锁定在 <b>ttl</b>指定的秒数后自动消失。<br/>
   * 该操作不抛出异常。
   *
   * @param lockable   lockable
   * @param lockerName 锁定者的名称
   * @return 是否成功锁定
   */
  def lock(lockable: Lockable, lockerName: String): Lock

  /**
   * 判断给定的锁当前是否被任意应用锁定。<br/>
   * 该操作不抛出异常。
   *
   * @param lockId 锁ID
   * @return 是否已被锁定，当底层服务异常（连接异常）时，返回null。
   */
  def isLocked(lockId: String): java.lang.Boolean

  /**
   * 判断给定的锁是否被某个指定的锁定者锁定。
   * 该操作不抛出异常。
   *
   * @param lockId
   * 锁ID
   * @param lockerName
   * 锁定者名称
   * @return 是否已被锁定，当底层服务异常（连接异常）时，返回null。
   */
  def isLockedBy(lockId: String, lockerName: String): java.lang.Boolean

  /**
   * 对给定的锁名进行解锁。当前锁的锁定者信息须与给定锁定者信息完全一致方能解锁。<br/>
   * 该操作不抛出异常。
   *
   * @param lockId
   * 锁ID
   * @param lockerInfo
   * 锁定者信息
   * @return 解锁结果（SUCCESS/NOT_LOCKED/FAILED）。
   */
  def unlock(lockId: String, lockerInfo: String): LockService.UnlockResult

  /**
   * 尝试保持锁定。锁的生命时长将更新为新的时长（从命令成功开始计时）。<br/>
   * 该操作不抛出异常。
   *
   * @param oldLock
   * 旧锁
   * @return 新的锁对象。如果锁不存在或不是由给定的锁定者锁定则返回null。
   */
  def keepLock(oldLock: Lock): Lock

  /**
   * 执行全局计数器的增加。 该操作不抛出异常。
   *
   * @param counterId
   * 全局计数器ID
   * @param increment
   * 增加量
   * @param timeoutInMillis
   * 操作超时时间
   * @return 增加后的值，当连接异常或超时则返回null。
   */
  def incCounter(counterId: String, increment: Int, timeoutInMillis: Int): java.lang.Long

}

object LockService {

  sealed trait UnlockResult

  /**
   * 成功解锁
   */
  case object SUCCESS extends UnlockResult

  /**
   * 指定的锁不是被指定的锁定者锁定或未被任何应用锁定
   */
  case object NOT_LOCKED extends UnlockResult

  /**
   * 解锁失败，可能是因为尝试解锁时，锁已经被其他应用锁定或解锁
   */
  case object FAILED extends UnlockResult

  def lockInfo(lockerName: String, lockTime: Long): String = {
    val dt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(lockTime), Consts.ZONE_ID_Z)
    lockerName + "@" + dt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }
}
