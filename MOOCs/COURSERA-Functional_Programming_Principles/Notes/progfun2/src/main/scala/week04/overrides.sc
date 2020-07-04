object overrides {
  println("Welcoem to the Scala worksheet")
}

abstract class Base {
  def foo = 1
  def bar: Int
}

//Sub is SUBCLASS inheriting from Base SUPERCLASS
class Sub extends Base {
  //Override keyword is necessary, since you are replacing standard definition
  override def foo = 2
  //Unnecessary because foo2 is a different variable
  def foo2 = 3
  def bar = 3
}