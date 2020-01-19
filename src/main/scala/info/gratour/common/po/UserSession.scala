package info.gratour.common.po

trait UserSession {

  def getToken: String
  def isExpired: Boolean
  def updateExpireTime(): Unit
}
