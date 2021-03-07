package info.gratour.common.types

import java.util
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean


class QueueBackedIterator[E <: AnyRef](val capacity: Int) extends util.Iterator[E] with Sink[E] {

  val queue: ArrayBlockingQueue[E] = new ArrayBlockingQueue[E](capacity)
  val eofFlag: AtomicBoolean = new AtomicBoolean()

  override def hasNext: Boolean = !eofFlag.get()

  override def next(): E =
    queue.take()

  override def offer(e: E): Unit = queue.put(e)

  override def eof(): Unit = eofFlag.set(true)

  def toSink[SEND <: AnyRef](mapper: SEND => E): Sink[SEND] =
    new Sink[SEND] {
      override def offer(e: SEND): Unit = QueueBackedIterator.this.offer(mapper(e))

      override def eof(): Unit = QueueBackedIterator.this.eof()
    }
}

object QueueBackedIterator {
  def apply[E <: AnyRef](capacity: Int): QueueBackedIterator[E] = new QueueBackedIterator[E](capacity)
}
