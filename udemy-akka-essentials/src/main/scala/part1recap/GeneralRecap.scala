package part1recap

// 'extends App' means you can run as a standalone application
object GeneralRecap extends App {
  //values CANNOT be reassigned
  val aCondition: Boolean = false

  //variables CAN be reassigned. Scala compiler guesses type
    //You can, however, explicitly refer types if you want
  var aVariable = 42
  var bVariable: Int = 42
  aVariable += 1

  //expressions
  val aConditionedVal = if (aCondition) 42 else 65

  //code block
  val aCodeBlock = {
    if (aCondition) 74
    else 56
  }

  //TYPE: Unit
    //Does something but DOESN'T return any meaningful value
  val theUnit = println("Hello, Scala")

  def aFunction(x: Int): Int = x + 1

  //RECURSION: Tail Recursion
    //Reuses stack frame to mitigate stack overflow
  def factorial(n: Int, acc: Int): Int = {
      if (n <= 0) acc
      else factorial(n-1, acc * n)
    }

  //OBJECT ORIENTED PROGRAMMING
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  //METHOD NOTATION: Infix Notation
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  //CLASSES: Anonymous Classes

  //val aCarnivore = new Carnivore
    //Commented out implementation invalid, as you need to supply method logic

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  aCarnivore eat aDog

  //Generics
}
