//Binary Tree Implementation

//Abstract Classes CANNOT be instantiated
abstract class IntSet {
  //Methods within abstract class CANNOT have method logic
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
}

//NOTE: In order to extend, child classes MUST have base parameters
class Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
}

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  //Check if proposed element is left or right on binary trees
  def contains(x: Int): Boolean =
    if(x < elem) left.contains(x)
    else if(x > elem) right.contains(x)
    else true

  def incl(x: Int): IntSet =
    if(x < elem) new NonEmpty(elem, left.incl(x), right)
    else if(x > elem) new NonEmpty(elem, left, right.incl(x))
    else this
}

object intsets {
  //You cannot instantiate abstract class
  //You need a class to extend abstract and then instantiate child
  println("Welcome to the Scala worksheet")
}