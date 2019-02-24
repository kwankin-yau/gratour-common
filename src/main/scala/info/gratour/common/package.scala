package info.gratour

import scala.reflect.ClassTag

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


}
