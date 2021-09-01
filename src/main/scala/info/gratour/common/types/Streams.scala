/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.types

import java.io.{DataInputStream, FilterInputStream, InputStream}

object Streams {

  def dataInputStreamEx(in: InputStream): DataInputStreamEx = new DataInputStreamEx(in)

}

class PositionalInputStream(in: InputStream) extends FilterInputStream(in) {
  protected var pos: Long = 0L
  protected var mark: Long = 0L

  def position: Long = pos

  override def read(): Int = {
    val b = super.read()
    if (b >= 0)
      pos += 1

    b
  }

  override def read(b: Array[Byte], off: Int, len: Int): Int = {
    val r = super.read(b, off, len)
    if (r >= 0)
      pos += r

    r
  }

  override def skip(n: Long): Long = {
    val r = super.skip(n)
    if (r >= 0)
      pos += r

    r
  }

  override def mark(readlimit: Int): Unit = {
    super.mark(readlimit)
    mark = pos
  }

  override def reset(): Unit = {
    if (!markSupported())
      throw new IllegalStateException("Mark not supported.")

    super.reset()
    pos = mark
  }

}

class DataInputStreamEx(str: InputStream) extends DataInputStream(new PositionalInputStream(str)) {

  def position(): Long = in.asInstanceOf[PositionalInputStream].position

}

