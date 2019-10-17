package info.gratour.common.utils

import java.nio.ByteBuffer
import java.util.{Locale, Random, UUID}

import sun.util.locale.LanguageTag

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object CommonUtils {

  def futureToTry[T](f: Future[T])(implicit ec: ExecutionContext): Future[Try[T]] =
    f.map(Success(_))
      .recover {
        case e: Throwable => Failure(e)
      }


  private val random = new Random(System.nanoTime)

  def randomBytes(bytes: Array[Byte]): Unit = {
    random.nextBytes(bytes)
  }


  def randomBase32(len: Int): String = {
    val base32 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val str = new StringBuilder()
    (1 to len).map(_ => {
      val idx = random.nextInt(32)
      val c = base32.charAt(idx)
      str += c
    })

    str.toString
  }

  def randomBase10(len: Int): String = {
    val base10 = "0123456789"
    val str = new StringBuilder()
    (1 to len).map(_ => {
      val idx = random.nextInt(10)
      val c = base10.charAt(idx)
      str += c
    })

    str.toString
  }

  def uuidToBytes(uuid: UUID): Array[Byte] = {
    val buff = ByteBuffer.wrap(new Array[Byte](16))
    buff.putLong(uuid.getMostSignificantBits)
    buff.putLong(uuid.getLeastSignificantBits)
    buff.array()
  }

  /**
    * zh-CN => (zh, CN)
    * zh_CN => (zh, CN)
    * en-US => (en, US)
    * en => (en, US)    -- default
    *
    * @param languageTag
    * @return null if parse failed
    */
  def parseLanguageTag(languageTag: String): LangTag = {
    if (languageTag == null)
      return null

    var idx = languageTag.indexOf('_')
    if (idx < 0)
      idx = languageTag.indexOf('-')

    if (idx > 0)
      LangTag(languageTag.substring(0, idx), languageTag.substring(idx + 1))
    else if (languageTag == "en")
      LangTag("en", "US")
    else
      null
  }
}

case class LangTag(langId: String, countryCode: String) {

  def toLocale: Locale = new Locale(langId, countryCode)

}
