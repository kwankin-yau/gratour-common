/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types.rest

import java.util

object RestTypes {

}

case class Pagination(
                       limit: Int,
                       page: Int // 1 based
                     ) {
  def offset: Int = (page - 1) * limit

  def nextPage(): Pagination = Pagination(limit, page + 1)

  def nextPage(totalRecordCount: Long): Pagination = {
    val nextOffset = offset + limit
    if (nextOffset < totalRecordCount)
      Pagination(limit, page + 1)
    else
      null
  }

  def listPaging[T](list: java.util.List[T]): java.util.List[T] = {
    val ofs = offset
    if (ofs >= list.size())
      new util.ArrayList[T]()
    else {
      list.subList(offset, offset + limit)
    }
  }

}

object Pagination {
  val DEFAULT: Pagination = Pagination(20, 1)
  val FIRST_ONE: Pagination = Pagination(1, 1)

  def pageCount(totalRecordCount: Long, pageSize: Int): Long = {
    var r = totalRecordCount / pageSize
    if ((totalRecordCount % pageSize) != 0) {
      r += 1
    }
    r
  }

}
