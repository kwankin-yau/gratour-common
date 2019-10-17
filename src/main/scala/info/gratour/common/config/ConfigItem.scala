package info.gratour.common.config

import com.typesafe.config.Config

/**
  *
  * @param name
  * @param defaultValue
  * @param readOnly
  * @param internal 如果是internal，则外部不可查阅
  * @param config
  */
abstract class ConfigItem[T](
    val name: String,
    val defaultValue: T,
    val readOnly: Boolean = false,
    val internal: Boolean = false
)(implicit val config: Config, implicit val configPath: ConfigItem.ConfigPath) {

  @volatile private var value: T = _

  protected def fromString(value: String): T = ???

  def init(): Unit = {
    val actualConfig = {
      if (config != null) {
        if (config.hasPath(configPath)) config.getConfig(configPath) else null
      } else
        null
    }

    if (actualConfig != null && actualConfig.hasPath(name)) {
      value = fromString(actualConfig.getString(name))
      if (value == null)
        value = defaultValue
    } else {
      val v = System.getProperty(name)
      if (v != null)
        value = fromString(v)
      else
        value = defaultValue
    }
  }

  def get(): T = {
    value
  }

  def set(newValue: T): Unit = {
    if (readOnly)
      throw new RuntimeException(
        "Could not change value of the read only config item.")

    value = newValue
  }

  init()
}

object ConfigItem {
  type ConfigPath = String
}

class IntConfigItem(override val name: String,
                    override val defaultValue: Int,
                    override val readOnly: Boolean = false,
                    override val internal: Boolean = false)(
    implicit override val config: Config,
    implicit override val configPath: ConfigItem.ConfigPath)
    extends ConfigItem[Int](name, defaultValue, readOnly, internal) {
  override def fromString(value: String): Int = {
    value.toInt
  }
}

class LongConfigItem(override val name: String,
                     override val defaultValue: Long,
                     override val readOnly: Boolean = false,
                     override val internal: Boolean = false)(
    implicit override val config: Config,
    implicit override val configPath: ConfigItem.ConfigPath)
    extends ConfigItem[Long](name, defaultValue, readOnly, internal) {
  override protected def fromString(value: String): Long = {
    value.toLong
  }
}

class DoubleConfigItem(override val name: String,
                       override val defaultValue: Double,
                       override val readOnly: Boolean = false,
                       override val internal: Boolean = false)(
    implicit override val config: Config,
    implicit override val configPath: ConfigItem.ConfigPath)
    extends ConfigItem[Double](name, defaultValue, readOnly, internal) {
  override protected def fromString(value: String): Double = {
    value.toDouble
  }
}

class BooleanConfigItem(override val name: String,
                        override val defaultValue: Boolean,
                        override val readOnly: Boolean = false,
                        override val internal: Boolean = false)(
    implicit override val config: Config,
    implicit override val configPath: ConfigItem.ConfigPath)
    extends ConfigItem[Boolean](name, defaultValue, readOnly, internal) {
  override protected def fromString(value: String): Boolean = {
    value.toBoolean
  }
}

class StringConfigItem(override val name: String,
                       override val defaultValue: String,
                       override val readOnly: Boolean = false,
                       override val internal: Boolean = false)(
    implicit override val config: Config,
    implicit override val configPath: ConfigItem.ConfigPath)
    extends ConfigItem[String](name, defaultValue, readOnly, internal) {
  override protected def fromString(value: String): String = {
    value
  }
}
