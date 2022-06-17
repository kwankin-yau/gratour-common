/** *****************************************************************************
 * Copyright (c) 2019, 2022 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ***************************************************************************** */
package info.gratour.common.types

class MovingAverageD(size: Int) {

  private var count = 0
  private var total = 0d
  private var index = 0
  private val samples: Array[Double] = new Array[Double](size)

  def collect(value: Double): Unit = {
    total -= samples(index)
    samples(index) = value
    total += value
    if (count < size)
      count += 1

    index += 1
    if (index == size)
      index = 0
  }

  def average(): Double = {
    if (count > 0)
      total / count
    else
      0d
  }
}

class MovingAverageL(size: Int) {

  private var count = 0
  private var total = 0L
  private var index = 0
  private val samples: Array[Long] = new Array[Long](size)

  def collect(value: Long): Unit = {
    total -= samples(index)
    samples(index) = value
    total += value
    if (count < size)
      count += 1

    index += 1
    if (index == size)
      index = 0
  }

  def average(): Double = {
    if (count > 0)
      total.toDouble / count
    else
      0d
  }
}
