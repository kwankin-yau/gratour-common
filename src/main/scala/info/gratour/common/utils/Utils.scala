package info.gratour.common.utils

import java.io.File
import java.net.URL
import java.time.{ZoneId, ZoneOffset}

import org.apache.commons.io.FilenameUtils

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Utils {

  def futureToTry[T](f: Future[T])(implicit ec: ExecutionContext): Future[Try[T]] =
    f.map(Success(_))
      .recover {
        case e: Throwable => Failure(e)
      }


  def appendPathSeparator(path: String): String = {
    if (path == null || path.length == 0)
      File.separator
    else {
      if (!path.endsWith("/") && !path.endsWith("\\"))
        path + File.separator
      else
        path
    }
  }

  def getJarDir(clzz: Class[_]): File = {
    var url: URL = null

    // get an url
    try {
      url = clzz.getProtectionDomain.getCodeSource.getLocation
      // url is in one of two forms
      //        ./build/classes/   NetBeans test
      //        jardir/JarName.jar  froma jar
    } catch {
      case ex: SecurityException =>
        url = clzz.getResource(clzz.getSimpleName + ".class")
      // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
      //          file:/U:/Fred/java/Tools/UI/build/classes
      //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    var extURL = url.toExternalForm
    if (extURL.endsWith(".jar")) // from getCodeSource
      extURL = extURL.substring(0, extURL.lastIndexOf('/'))
    else {
      // from getResource
      val suffix = "/" + clzz.getName.replace('.', '/') + ".class"
      extURL = extURL.replace(suffix, "")
      if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
        extURL = extURL.substring(4, extURL.lastIndexOf("/"))
    }

    try {
      new File(url.toURI)
    } catch {
      case t: Throwable =>
        new File(url.getPath)
    }
  }

  def codeSourcePath(clzz: Class[_]): String = {
    //    val path = Utils.getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    //    val decodedPath = URLDecoder.decode(path, "UTF-8")
    val path = getJarDir(clzz)
    val pathString = path.getAbsolutePath
    FilenameUtils.normalize(appendPathSeparator(pathString))
  }

  /**
    * Get ZoneId of specified id string.
    *
    * @return null if zoneId not found or invalid.
    */
  def zoneIdOf(zoneId: String): ZoneId = {
    if (zoneId == null || zoneId.isEmpty)
      return null

    try {
      ZoneId.of(zoneId)
    } catch {
      case _: Exception =>
        null
    }
  }

  /**
    * Get ZoneId of specified zone offset(by minutes).
    *
    * @return null if offset out of range.
    */
  def zoneIdOfOffset(zoneOffsetMinutes: Int): ZoneId = {
    try {
      ZoneOffset.ofTotalSeconds(zoneOffsetMinutes * 60)
    } catch {
      case _: Exception =>
        null
    }
  }
}
