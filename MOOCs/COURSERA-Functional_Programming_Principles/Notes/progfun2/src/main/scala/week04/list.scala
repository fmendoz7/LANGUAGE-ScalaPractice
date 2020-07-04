package week04

//import week04.Boolean

//Function creating a list consisting of a single element
//def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

class Nil[T] extends List[T] {
  def isEmpty = true

  //Exceptions have type NOTHING
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

/*
  TYPE PARAMETER
    - Highly generalizable parameter to define trait or class with different types
    - DO NOT affect evaluation in Scala
    [T]
*/

/*
  TYPE ERASURE
    - Assume all type parameters and type arguments are removed before evaluating the program
*/

/*
  POLYMORPHISM
    - Two principle forms of polymorphism
      > SUBTYPING
        - Came first from OOP languages
        - Instances of a subclass can be passed to a base class
      > GENERICS
        - Came first from FUNCTIONAL languages
        - Instances of a function or class are created by type parameterization
*/