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