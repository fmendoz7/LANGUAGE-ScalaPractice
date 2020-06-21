import week04._

object cons {

}

class Cons1(val head: Int, val tail: IntList) extends IntList
  //Defines parameters and fields of class at the same time
  //Equivalent to Cons2

class Cons2(_head: Int, _tail: IntList) extends IntList {
  //Assuming _head and _tail are unusued names
  val head = _head
  val tail = _tail
}

/*
  TYPE PARAMETER
    - Highly generalizable parameter to define trait or class with different types
    [T]
*/