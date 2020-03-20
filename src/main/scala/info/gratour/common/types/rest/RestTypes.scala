/** *****************************************************************************
 * Copyright (c) 2019, 2020 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.types.rest

object RestTypes {

}

case class Pagination(
                       limit: Int,
                       page: Int // 1 based
                     ) {
  def offset: Int = (page - 1) * limit
}

object Pagination {
  val DEFAULT: Pagination = Pagination(20, 1)
}
