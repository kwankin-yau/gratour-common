/** *****************************************************************************
 * Copyright (c) 2019, 2021 lucendar.com.
 * All rights reserved.
 *
 * Contributors:
 * KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 * ******************************************************************************/
package info.gratour.common.service

import java.io.Closeable
import com.typesafe.scalalogging.Logger

import java.util.concurrent.atomic.{AtomicBoolean, AtomicReference}

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
  private val startup: AtomicBoolean = new AtomicBoolean()
  private val delayedShutdown: AtomicBoolean = new AtomicBoolean()
  private val shutdownExecuted: AtomicBoolean = new AtomicBoolean()

  final val list: java.util.List[Entry] = new java.util.ArrayList[Entry]()

  def beginStartup(): Unit = {
    startup.set(true)
  }

  def endStartup(): Unit = {
    startup.set(false)
    if (delayedShutdown.get()) {
      logger.debug("Execute delayed shutdown.")
      shutdown()
    }
  }

  def shutdown(): Unit = {
    if (startup.get()) {
      logger.debug("shutdown() called in startup processing, delay it.", new Throwable)
      delayedShutdown.set(true)
      return
    }

    if (shutdownExecuted.compareAndSet(false, true)) {

      this.synchronized {
        logger.debug("Starting shutdown() processing, registry: ")
        list.forEach(e => {
          logger.debug("  " + e.name)
        })

        list.forEach { e =>
          e.close()
        }
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

  private class ShutdownRunnable(delaySeconds: Int) extends Runnable {
    override def run(): Unit = {
      if (delaySeconds > 0)
        Thread.sleep(delaySeconds * 1000)

      shutdown()
    }
  }

  def scheduleShutdown(delaySeconds: Int): Unit = {
    val t = new Thread(new ShutdownRunnable(delaySeconds))
    t.start()
  }

  private class DelayedRunnable(delaySeconds: Int, runnable: Runnable) extends Runnable {
    override def run(): Unit = {

      if (delaySeconds > 0)
        Thread.sleep(delaySeconds * 1000)

      runnable.run()
    }
  }

  def delayRun(delaySeconds: Int, runnable: Runnable): Unit = {
    val t = new Thread(new DelayedRunnable(delaySeconds, runnable))
    t.start()
  }

}
