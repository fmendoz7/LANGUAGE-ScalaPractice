package part1recap

// ._ import notation imports EVERYTHING from that package
import scala.util._

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

  //----------------------------------------------------------

  //CLASSES: Anonymous Classes

  //val aCarnivore = new Carnivore
    //Commented out implementation invalid, as you need to supply method logic

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  aCarnivore eat aDog

  //GENERICS
  abstract class MyList[+A]

  //Companion Objects
  object MyList

  //Singleton Objects

  //Case Classes
  case class Person(name: String, age: Int)

  //Exceptions
  val aPotentialFailure = try {
    throw new RuntimeException("I'm Innocent, I Swear!")
    }
    //This returns nothing

    catch {
      case e: Exception => "I caught an exception!"
    }

    finally {
      //side effects, happen no matter what
      println("Some Logs.. dododo")
    }

  //----------------------------------------------------------
  //FUNCTIONAL PROGRAMMING

  //Object Oriented Style for function incrementing a value
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  val incremented = incrementer(42) //43
  //Scala Compiler Trick: when it sees value being called like an object,
  //automatically applies apply method

  //Anonymous/Lambda Function
    // Int => Int === Function1[Int, Int]
  val anonymousIncrementer = (x: Int) => x + 1

  //Functional Programming is ALL ABOUT working with functions as first class
  List(1,2,3).map(incrementer)

  val pairs = for {
    num <- List(1,2,3,4)
    char <- List('a','b','c','d')
  } yield num + "-" + char

  //List(1,2,3,4).flatMap(num => List('a','b','c','d').map(char => num + "-" + char))

  //Sequences, Array, List, Vector, Map, Tuples, Sets

  //Collections
    //Option and Try
  val anOption = Some(2)
  val aTry = Try {
    throw new RuntimeException
  }

  //PATTERN MATCHING
  val unknown = 2
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

}
