package week05

//import week05.Boolean

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
    //Remember that tail is EVERYTHING BUT the head (first element)
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

class Nil[T] extends List[T] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

object List {
  // List(1,2) = List.apply(1,2)
    //Type 'T' really just means 'any'. It's a generalizable type
  //Essentially, have node head x1 and tail node linking to
  def apply[T](x1: T, x2: T): List[T] = new Cons(x1, new Cons(x2, new Nil))

  //Overloading. If no parameters, null list
  def apply[T]() = new Nil
}

/*
  IMPORTANT PROPERTIES OF LSITS IN SCALA
    - Exclusively in Scala:
        > Lists are IMMUTABLE
        > Lists are RECURSIVE
    - Lists elements have this structure

    -----------------------------
    | element | pointer to next |
    -----------------------------

    EX:
      val fruit: List[String] = List("apples", "oranges", "pears")
      val nums: List[Int] = List(1, 2, 3, 4)
      val diag3: List[List[Int]] = List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1))
      val empty: List[Nothing] = List()

    - Lists are constructed from
        > Empty list Nil
        > Construction operation ::

    EX:
      fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
      nums = 1 :: (2 :: (3 :: (4 :: Nil)))
      empty = Nil

      A :: B :: C == A :: (B :: C)
*/

/*
  SCALA LIST PATTERN MATCHING
    - Nil
        > The Nil Constant
    - p :: ps
        > Pattern matching a list with head matching p, tail matching ps

    EX:
      List(p1, ..., pn)
        //the same as p1 :: ... :: pn :: Nil, or p1 :: ...(pn :: Nil)
          //Have n - 1 braces succeeding Nil
*/