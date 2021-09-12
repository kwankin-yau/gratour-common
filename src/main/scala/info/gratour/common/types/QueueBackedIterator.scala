package info.gratour.common.types

import java.util.Iterator
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class QueueBackedIterator[E](val queue: ArrayBlockingQueue[E], val eof: AtomicBoolean) extends Iterator[E] {

  override def hasNext: Boolean = !eof.get()

  override def next(): E =
    queue.take()
}
