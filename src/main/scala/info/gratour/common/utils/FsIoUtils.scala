package info.gratour.common.utils

import java.io._
import java.net.URL
import java.util.function.Consumer
import org.apache.commons.io.{FileUtils, FilenameUtils}

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, Files, Path, Paths, SimpleFileVisitor}
import java.util.concurrent.atomic.AtomicLong

object FsIoUtils {

  def byteCountToDisplaySize(size: Long): String = FileUtils.byteCountToDisplaySize(size)


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

    url = new URL(extURL);

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
   * 读取流中的字符串并按行调用 lineConsumer，完成后关闭 inputStream
   *
   * @param inputStream  输入流，方法调用后流将被关闭
   * @param lineConsumer 字符串行的消费者
   */
  def readLinesAndClose(inputStream: InputStream, lineConsumer: Consumer[String]): Unit = {
    val reader = new BufferedReader(new InputStreamReader(inputStream))
    try
      reader.lines.forEach(lineConsumer)
    catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
    finally if (reader != null) reader.close()

  }

  /**
   * 查询目录下的所有文件的大小总和，包括子目录
   *
   * @param dir 所要查询的目录
   * @return 文件大小总和，字节
   */
  def folderFilesTotalSize(dir: String): Long = {
    val sz = new AtomicLong(0)

    try {
      Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor[Path]() {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
          sz.addAndGet(attrs.size())
          FileVisitResult.CONTINUE
        }

        override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE

        override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
      })
    } catch {
      case _: Throwable =>
    }

    sz.get()
  }

}

