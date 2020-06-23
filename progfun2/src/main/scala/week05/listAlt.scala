package week05

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
  def prepend [U >: T] (elem: U): List[U] = new Cons(elem, this)
    //SUPERCLASS T, SUBCLASS U
    //elem is of type U
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

object Nil extends List[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

object test {
  val x: List[String] = Nil
  def f(xs: List[NonEmpty], x: Empty) = xs.prepend(x)
}

/*
  VARIANCES IN SCALA:
    - Covariance [+T]
        > If A is a subtype of B, List[A] subtype of List[B]
    - Contravariance [-T]
        > A is a subtype of B, List[B] is a subtype of List[A]
    - Nonvariance [T]

    Arrow points to the supertype, from the subtype
 */

/*
  Sometimes, we have to put in a bit of work to make a class covariant

  Ex:
    trait List[+T] {
      def prepend(elem: T): List[T] = new Cons(elem, this)
    }

    - Code does NOT type-check because prepend fails variance checking
    - Prepend also violates Liskov Substitution Principle
 */