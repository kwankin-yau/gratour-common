package info.gratour

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.matching.Regex

package object common {

  //  implicit class ListFinder[+T](list: java.util.List[T]) {
  //    def find(p: T => Boolean): Option[T] = {
  //      for (i <- 0 until list.size()) {
  //        val t = list.get(i)
  //        if (p(t))
  //          return Some(t)
  //      }
  //
  //      None
  //    }
  //
  //    def exists(p: T => Boolean): Boolean = {
  //      for (i <- 0 until list.size()) {
  //        val t = list.get(i)
  //        if (p(t))
  //          return true
  //      }
  //
  //      false
  //    }
  //  }

  implicit class CollectionFinder[+T](collection: java.util.Collection[T]) {
    def find(p: T => Boolean): Option[T] = {
      val iter = collection.iterator()
      while (iter.hasNext) {
        val t = iter.next()
        if (p(t))
          return Some(t)
      }

      None
    }

    def exists(p: T => Boolean): Boolean = {
      val iter = collection.iterator()
      while (iter.hasNext) {
        val t = iter.next()
        if (p(t))
          return true
      }

      false
    }

  }

  implicit class PathExtractor(sc: StringContext) {

    object path {
      def unapplySeq(str: String): Option[Seq[String]] =
        sc.parts.map(Regex.quote).mkString("^", "([^/]+)", "$").r.unapplySeq(str)
    }

  }

  def convertFuture[T](x: Future[T]): java.util.concurrent.Future[T] = {
    new java.util.concurrent.Future[T] {
      override def isCancelled: Boolean = throw new UnsupportedOperationException

      override def get(): T = Await.result(x, Duration.Inf)

      override def get(timeout: Long, unit: TimeUnit): T = Await.result(x, Duration.create(timeout, unit))

      override def cancel(mayInterruptIfRunning: Boolean): Boolean = throw new UnsupportedOperationException

      override def isDone: Boolean = x.isCompleted
    }
  }

}
