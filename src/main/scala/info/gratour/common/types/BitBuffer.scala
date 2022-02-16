/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.types

import java.nio.ByteBuffer

import scala.collection.mutable.ArrayBuffer

class BitBuffer(source: BitBufferSource) {

  private var buffer: Long = 0
  private var remainBits: Int = 0

  def readBits(bitCount: Int): Long = {
    if (bitCount > 64)
      throw new IllegalArgumentException("bitCount")

    var r: Long = 0
    var remainToFetch = bitCount

    if (remainToFetch >= remainBits) {
      r = buffer
      buffer = 0
      remainBits = 0
      remainToFetch -= remainBits
    }

    while (remainToFetch > 0) {
      val (bits, value) = {
        if (remainToFetch == 64)
          (64, source.readLong())
        else if (remainToFetch >= 32)
          (32, source.readInt().toLong)
        else if (remainToFetch >= 16)
          (16, source.readShort().toLong)
        else
          (8, source.readByte().toLong)
      }

      buffer = value
      remainBits = bits

      val toFetch =
        if (remainToFetch > bits)
          bits
        else
          remainToFetch

      remainToFetch -= toFetch
      remainBits -= toFetch

      r <<= toFetch
      val v = buffer >>> remainBits
      r |= v
      buffer &= BitBuffer.MASKS(remainBits)

      if (remainBits > 0)
        buffer >>>= toFetch
      else
        buffer = 0
    }

    r
  }


  def readBit(): Int = readBits(1).toInt

  def readBitBool(): Boolean = readBit() != 0

  def readByte(): Byte =
    if (remainBits == 0)
      source.readByte()
    else
      readBits(8).toByte

  def readShort(): Short =
    if (remainBits == 0)
      source.readShort()
    else
      readBits(16).toShort

  def readInt(): Int =
    if (remainBits == 0)
      source.readInt()
    else
      readBits(32).toInt

  def readLong(): Long =
    if (remainBits == 0)
      source.readLong()
    else
      readBits(64)

  def readSingle(): Float =
    if (remainBits == 0)
      java.lang.Float.intBitsToFloat(source.readInt())
    else
      java.lang.Float.intBitsToFloat(readInt())

  def readDouble(): Double =
    if (remainBits == 0)
      java.lang.Double.longBitsToDouble(source.readLong())
    else
      java.lang.Double.longBitsToDouble(readLong())

}

object BitBuffer {

  private final val MASKS: Array[Long] = {
    val buff = ArrayBuffer.empty[Long]
    var v: Long = 0
    for (_ <- 0 to 63) {
      v = v << 1
      v = v | 1
      buff += v
    }
    buff.toArray
  }


}

trait BitBufferSource {

  def readByte(): Byte

  def readShort(): Short

  def readInt(): Int

  def readLong(): Long

}

class ByteBufferAsBitBuffer(byteBuf: ByteBuffer) extends BitBufferSource {
  override def readByte(): Byte = byteBuf.get()

  override def readShort(): Short = byteBuf.getShort

  override def readInt(): Int = byteBuf.getInt

  override def readLong(): Long = byteBuf.getLong
}
