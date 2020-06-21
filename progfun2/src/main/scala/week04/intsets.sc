//Binary Tree Implementation

//Abstract Classes CANNOT be instantiated
  //ITC, intset is the SUPERCLASS of EMPTY and NONEMPTY SUBCLASSES
abstract class IntSet {
  //Methods within abstract class CANNOT have method logic
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
  def union(other: IntSet): IntSet
}

//NOTE: In order to extend, child classes MUST have base parameters
class Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  def union(other: IntSet): IntSet = other
  override def toString: String = "."
}

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  //Check if proposed element is left or right on binary trees
  override def toString = "{" + left + elem + right + "}"
  def contains(x: Int): Boolean =
    if(x < elem) left.contains(x)
    else if(x > elem) right.contains(x)
    else true

  def incl(x: Int): IntSet =
    if(x < elem) new NonEmpty(elem, left.incl(x), right)
    else if(x > elem) new NonEmpty(elem, left, right.incl(x))
    else this

  def union(other: IntSet): IntSet =
    ((left union right) union other) incl elem
}

object intsets {
  //You cannot instantiate abstract class
  //You need a class to extend abstract and then instantiate child
  val t1 = new NonEmpty(3, new Empty, new Empty)
  val t2 = t1.incl(4)
}

//CONCEPTUAL
/*
    PERSISTENT DATA STRUCTURES: Upon making changes, old data structure still maintained
        IE: Add node 3 to binary tree. Instead of appending to old data structure..
          #1: New nodes get created up the tree, make root node of 7, right child links to original 12

          OLD TREE:
                  7
              5       12
            E   E   9   13
                  E   E E   E

          NEW TREE:
                7
              5   *links to old tree's 12*
            3   E
           E  E
*/