package info.gratour.common.po

import java.lang.reflect.Type
import java.time.{LocalDate, LocalDateTime, OffsetDateTime}
import java.time.format.DateTimeFormatter

import com.google.gson.reflect.TypeToken
import info.gratour.common.error.{ErrorWithCode, Errors}

case class ScopedSysParamName(scope: String, paramName: String) {
  def toQualifiedParamName: String = SysParamInfo.qualifyParamName(scope, paramName)
}

object ScopedSysParamName {
  def apply(scope: String, paramName: String): ScopedSysParamName = new ScopedSysParamName(scope, paramName)

  def parse(qualifiedParamName: String): ScopedSysParamName = {
    val index = qualifiedParamName.indexOf('/')
    if (index <= 0)
      throw ErrorWithCode.internalError(s"Invalid qualified system parameter name: `$qualifiedParamName`.")

    ScopedSysParamName(qualifiedParamName.substring(0, index), qualifiedParamName.substring(index + 1))
  }
}

case class SysParamInfo(scope: String, paramName: String, paramValue: String) {

  if (!SysParamInfo.isValidScope(scope))
    throw new ErrorWithCode(Errors.INTERNAL_ERROR, s"Invalid system parameter scope: `$scope`.")

  if (!SysParamInfo.isValidParamName(paramName))
    throw new ErrorWithCode(Errors.INTERNAL_ERROR, s"Invalid system parameter name: `$paramName`.")

  def qualifiedParamName(): String = SysParamInfo.qualifyParamName(scope, paramName)

  def valueAsBoolean: Boolean = paramValue != null && paramValue.toBoolean

  def valueAsInt: Int = paramValue.toInt

  def valueAsLong: Long = paramValue.toLong

  def valueAsDouble: Double = paramValue.toDouble

  def valueAsLocalDate: LocalDate =
    if (paramValue != null)
      LocalDate.parse(paramValue, DateTimeFormatter.ISO_LOCAL_DATE)
    else
      null

  def valueAsLocalDateTime: LocalDateTime =
    if (paramValue != null)
      LocalDateTime.parse(paramValue, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    else
      null

  def valueAsOffsetDateTime: OffsetDateTime =
    if (paramValue != null)
      OffsetDateTime.parse(paramValue, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    else
      null
}

object SysParamInfo {
  val SYS_PARAM_INFO_TYPE: Type = new TypeToken[SysParamInfo]() {}.getType

  def apply(scope: String, paramName: String, paramValue: Boolean): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.toString)

  def apply(scope: String, paramName: String, paramValue: Int): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.toString)

  def apply(scope: String, paramName: String, paramValue: Long): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.toString)

  def apply(scope: String, paramName: String, paramValue: Double): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.toString)

  def apply(scope: String, paramName: String, paramValue: String): SysParamInfo = new SysParamInfo(scope, paramName, paramValue)

  def apply(scope: String, paramName: String, paramValue: LocalDate): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.format(DateTimeFormatter.ISO_LOCAL_DATE))

  def apply(scope: String, paramName: String, paramValue: LocalDateTime): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

  def apply(scope: String, paramName: String, paramValue: OffsetDateTime): SysParamInfo = new SysParamInfo(scope, paramName, paramValue.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))

  private def isValidIdent(ident: String): Boolean = {
    if (ident != null && ident.nonEmpty)
      ident.chars().allMatch(_.toChar != '/')
    else
      false
  }

  def isValidScope(scope: String): Boolean = isValidIdent(scope)


  def isValidParamName(paramName: String): Boolean = isValidIdent(paramName)

  def qualifyParamName(scope: String, paramName: String): String = scope + "/" + paramName

}
