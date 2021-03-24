package info.gratour.common.utils

import java.lang.reflect.{Field, Modifier}
import java.nio.ByteBuffer
import java.util.{Base64, Locale, Random, UUID}

import com.typesafe.scalalogging.Logger

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object CommonUtils {

  val ResLeakDetectLogger: Logger = Logger("ResLeakDetect")



  def futureToTry[T](f: Future[T])(implicit ec: ExecutionContext): Future[Try[T]] =
    f.map(Success(_))
      .recover {
        case e: Throwable => Failure(e)
      }


  private val random = new Random(System.nanoTime)

  def randomBytes(bytes: Array[Byte]): Unit = {
    random.nextBytes(bytes)
  }

  def randomBytesBase64(byteSize: Int = 16): String = {
    val bytes = new Array[Byte](byteSize)
    randomBytes(bytes)
    Base64.getUrlEncoder.encodeToString(bytes)
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
    val r = new Array[Byte](16)
    val buff = ByteBuffer.wrap(r)
    buff.putLong(uuid.getMostSignificantBits)
    buff.putLong(uuid.getLeastSignificantBits)
    r
  }

  def randomUuidString(): String = {
    val uuid = UUID.randomUUID()
    uuid.toString
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

  private def internalGetFields(clzz: Class[_], result: ArrayBuffer[Field]): Unit = {
    clzz.getDeclaredFields.foreach(f => {
      if (!Modifier.isStatic(f.getModifiers)) {
        result += f
      }
    })

    if (clzz.getSuperclass != classOf[Object])
      internalGetFields(clzz.getSuperclass, result)
  }

  def getInstanceFields(clzz: Class[_]): ArrayBuffer[Field] = {
    val r = ArrayBuffer.empty[Field]
    internalGetFields(clzz, r)
    r
  }

  def stringCompare(a: String, b: String): Int = {
    if (a != null) {
      if (b == null)
        1
      else
        a.compareTo(b)
    } else if (b != null)
      -1
    else
      0
  }

  def intCompare(a: Int, b: Int): Int = a - b

  def longCompare(a: Long, b: Long): Int = {
    if (a > b)
      1
    else if (a < b)
      -1
    else
      0
  }

  def boolCompare(a: Boolean, b: Boolean): Int = {
    if (a) {
      if (b)
        0
      else
        1
    } else {
      if (b)
        -1
      else
        0
    }
  }


  def getBackingArrayRange(byteBuffer: ByteBuffer): ByteArrayRange = {
    if (byteBuffer.hasArray) {
      val r = byteBuffer.array()
      val off = byteBuffer.arrayOffset()
      ByteArrayRange(r, off, byteBuffer.limit())
    } else {
      val size = byteBuffer.remaining()
      val r = new Array[Byte](size)
      if (size > 0)
        byteBuffer.get(r)
      ByteArrayRange(r, 0, size)
    }
  }

  def contactByteArrays(bs: Array[Byte]*): Array[Byte] = {
    val sz = bs.foldLeft(0)((c, b) => c + b.length)
    val r = new Array[Byte](sz)
    var index = 0
    bs.foreach(b => {
      System.arraycopy(b, 0, r, index, b.length)
      index += b.length
    })

    r
  }


}

case class ByteArrayRange(arr: Array[Byte], offset: Int, length: Int)

case class LangTag(langId: String, countryCode: String) {

  def toLocale: Locale = new Locale(langId, countryCode)

}

