# 3.2.6 - [2022-07-20]
## Improvement
- `info.gratour.common.utils.NetUtils.HttpUtils.parseAuthorization` added
- `info.gratour.common.types.rest.Pagination.DEFAULT_PAGE, DEFAULT_LIMIT, MAX_LIMIT, check()` added
- `info.gratour.common.utils.CachedIpResolver` added
- `info.gratour.common.types.DateTimeFmt` added
- `info.gratour.common.utils.DateTimeUtils`:
  - `millisToConvenientDateTimeString` added
  - `millisToConvenientDateTimeStringWithMillis` added
  - `dateTimeFmtOf` added
  - `millisToString` added
  - `parseDateTime(Long, DateTimeFmt)` added

# 3.2.5 - [2022-03-15]
## Added
- `info.gratour.common.types.MovingAverageD`
- `info.gratour.common.types.MovingAverageL`

## Fixed
- Add missing httpMethod parameter to `info.gratour.common.utils.NetUtils.HttpUtils.calcDigestAuthorization`

## Improvement
- `info.gratour.common.types.rest.Reply.ILLEGAL_STATE` added
- `info.gratour.common.types.rest.Reply.EXECUTION_ERROR` added
- `info.gratour.common.types.rest.Reply.INVALID_TOKEN` added
- `info.gratour.common.types.rest.RawReply.INVALID_TOKEN` added
- `info.gratour.common.types.rest.RawReply.ILLEGAL_STATE` added
- `info.gratour.common.types.rest.RawReply.INVALID_CONFIG` added
- `info.gratour.common.types.rest.RawReply.RawReply(info.gratour.common.error.ErrorWithCode)` added
- `info.gratour.common.types.rest.RawReply.err(info.gratour.common.error.ErrorWithCode)` added
- `info.gratour.common.error.ErrorWithCode.ILLEGAL_STATE` added
- `info.gratour.common.error.ErrorWithCode.INVALID_CONFIG` added
- `info.gratour.common.error.ErrorWithCode.TIMEOUT` added
- `info.gratour.common.error.ErrorWithCode.SERVICE_UNAVAILABLE` added
- `info.gratour.common.utils.CommonUtils.wrapIndex` added
- `info.gratour.common.utils.NetUtils.isValidDomain` added
- `info.gratour.common.utils.NetUtils.isValidIpOrDomain` added
- `info.gratour.common.types.rest.Reply.isEmpty` added
- `info.gratour.common.types.rest.Reply.nonEmpty` added
- `info.gratour.common.types.rest.Reply.SERVICE_UNAVAILABLE` added
- `info.gratour.common.types.validate.ValidateResultReceiver.error(java.lang.String)` added
- `info.gratour.common.utils.DateTimeUtils.DATETIME_FORMATTER_WITH_ZONE1` added
- `info.gratour.common.utils.DateTimeUtils.DATETIME_FORMATTER_WITH_MILLIS_ZONE1` added
- `info.gratour.common.utils.NetUtils.HttpUtils` added

## Changed
- Dependencies:
  - Bump `scala-library` from `2.13.6` to `2.13.8`
  - Bump `scala-reflect` from `2.13.6` to `2.13.8`

# 3.2.4 - [2022-01-02]
## Improvement
- `StringUtils.strMaxLen()` added.
- `RawReply.invalidParamRaw()`, `RawReply.TIMEOUT`, `Reply.TIMEOUT`, `ErrorWithCode.EXECUTION_ERROR`, `RawReply.EXECUTION_ERROR` added.
- `DateTimeUtils.tryStringToDate` added.
- `BeijingConv.tryStringToMillis`, `BeijingConv.tryStringToSeconds`, `BeijingConv.tryStringToOffsetDateTime` added.
- `JsonUtils` introduced. 
- Mark many scala constant to `final` to suggest compiler make inline optimization

## Fixed
- `Reply.single()` does not set the `count` field.
## Changed
- Dependencies:
  - `org.apache.commons:commons-lang3:3.11` => `org.apache.commons:commons-lang3:3.12.0`
  - `commons-io:commons-io:2.9.0` => `commons-io:commons-io:2.11.0`
  - `com.google.code.gson:gson:2.8.8` => `com.google.code.gson:gson:2.8.9`
  - `com.typesafe.scala-logging:scala-logging_2.13:3.9.2` => `com.typesafe.scala-logging:scala-logging_2.13:3.9.4`

# 3.2.3 - [2021-11-08]
## Added
- RawReply added `err(errCode)` and `err(errCode, message)` static method
- added `RawReply.SERVICE_UNAVAILABLE`
- added `Reply.INVALID_CONFIG`
- added `FsIoUtils.folderFilesTotalSize`
- added `EAbort`
- added `CommonUtils.touch()`

# 3.2.2 - 2021-10-15
## Changed
- Java version changed to 11.
- Some methods defined in `Errors` introduce locale parameter.
- Added `NetUtils.isLocalAddr()`, `NetUtils.isValidPortNum()`.
- Added `ValidateResultReceiver` interface and some implementation.
- Dependencies:
  - `dnsjava:dnsjava:3.3.1` => `dnsjava:dnsjava:3.4.2`, due to dns selector does not shut down when vm terminate.

## Added
- ErrorWithCode added `ok()` method.
- DateTimeUtils added `zoneIdOfOffsetHour`, `offsetDateTimeNowString()` methods.
- Reply added `error(Reply)`, `empty()` methods.

# [3.2.1] - 2021-09-05

## Added
- Errors.SERVICE_BUSY
- Errors.CODEC_ERROR
- Errors.CANCELED
- ErrorWithCode.format()

## Changed
- dependencies:
    - `org.scala-lang:scala-library:2.13.4` => `org.scala-lang:scala-library:2.13.6`
    - `org.scala-lang:scala-reflect:2.13.4` => `org.scala-lang:scala-reflect:2.13.6`
    - `com.google.code.gson:gson:2.8.6` => `com.google.code.gson:gson:2.8.8`
    - `org.apache.commons:commons-lang3:3.8.1` => `org.apache.commons:commons-lang3:3.11`
    - `commons-io:commons-io:2.6` => `commons-io:commons-io:2.9.0`    
