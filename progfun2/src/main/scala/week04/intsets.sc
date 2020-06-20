object intsets {
  println("Welcome to the Scala worksheet")
}

//Abstract Classes CANNOT be instantiated
abstract class IntSet {
  //Methods within abstract class CANNOT have method logic
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean
}