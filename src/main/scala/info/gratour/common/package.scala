package info.gratour

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


}
