# 3.2.4 - [2022-01-02]
## Improvement
- Added `StringUtils.strMaxLen()`
- Added `RawReply.invalidParamRaw()`, `RawReply.TIMEOUT`, `Reply.TIMEOUT`, `ErrorWithCode.EXECUTION_ERROR`, `RawReply.EXECUTION_ERROR`
- Added `DateTimeUtils.tryStringToDate`
- Added `BeijingConv.tryStringToMillis`, `BeijingConv.tryStringToSeconds`, `BeijingConv.tryStringToOffsetDateTime`
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
