package info.gratour.common.service

import java.io.Closeable

import com.typesafe.scalalogging.Logger

object ShutdownRegistry {

  private val logger = Logger(ShutdownRegistry.getClass.getName)

  case class Entry(name: String, resource: Object) {
    def close(): Unit = {
      logger.info(s"Closing resource : $name.")
      resource match {
        case closeable: Closeable => closeable.close()
        case autoCloseable: AutoCloseable => autoCloseable.close()
      }
    }
  }
  val list: java.util.List[Entry] = new java.util.ArrayList[Entry]()

  def shutdown(): Unit = {
    this.synchronized {
      list.forEach { e =>
        e.close()
      }
    }
  }

  def register(name: String, closeable: Closeable): Unit = {
    this.synchronized {
      list.add(Entry(name, closeable))
    }
  }

  def register(name: String, autoCloseable: AutoCloseable): Unit = {
    this.synchronized {
      list.add(Entry(name, autoCloseable))
    }
  }

}
