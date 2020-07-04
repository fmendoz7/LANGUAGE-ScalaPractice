package part1recap

import scala.concurrent.Future

// extends App ensures code is runnable
object AdvancedRecap extends App {
  //PARTIAL FUNCTIONS
    //Functions only runnable on a subset set of input domain
    //Does not provide an answer for any possible input value it could be given
    //Hence, provides an answer only for a subset of possible data, defining data IT CAN handle
  val partialFunction: PartialFunction[Int, Int] = {
      //Only accepts these values
      case 1 => 42
      case 2 => 65
      case 5 => 999
      //If any other value, will throw an exception
    }

  val pf = (x: Int) => x match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
  }

  val function: (Int => Int) = partialFunction

  val modifiedList = List(1,2,3).map {
    case 1 => 42
    case _ => 0
  }

  /*
  // ALTERNATIVE CASE of declaring modifiedList
  val modifiedList2 = List(1,2,3).map({
    case 1 => 42
    case _ => 0
  })
  */

  //-----------------------------------------------------------------------------------
  //LIFTING
  val lifted = partialFunction.lift
    //total function Int => Option[Int]
  lifted(2) //case for input of 2
  lifted(5000)  //None, will throw a matchError

  //orElse
  val pfChain = partialFunction.orElse[Int, Int] {
    case 60 => 9000
  }

  pfChain(5)    //999 per partialFunction
  pfChain(60)   // 9000
  pfChain(457)  //throws a MatchError, no case for 457
  // -----------------------------------------------------------------------------------
  //TYPE ALIASES
    // Definition: Literally make a self-defined type a literal alias of another partial function
  type ReceiveFunction = PartialFunction[Any, Unit]
    //ReceiveFunction is an alias for PartialFunction

  def receive: ReceiveFunction = {
    case 1 => println("hello")
    case _ => println("Anomalous Input Detected")
  }
//-----------------------------------------------------------------------------------
  //IMPLICITS
  //IMPLICIT Parameters
      // -
  implicit val timeout = 3000
  def setTimeout(f: () => Unit)(implicit timeout: Int) = f()

  setTimeout(() => println("timeout"))
    //extra parameter list omitted, since implicit value is defined before

  //IMPLICIT Conversions
    //1) Implicit defs
  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
    //Converts Person object called in front of greet method into a string
  }

  implicit def fromStringToPerson(string: String): Person = Person(string)
  "Peter".greet
    // fromStringToPerson("Peter").greet
    // This type conversion is done automatically by the compiler

  //IMPLICIT Classes
  implicit class Dog(name: String) {
    def bark = println("bark!")
  }

  "Lassie".bark
    // Converts DOG class into a string, then calling bark method
    // new Dog("Lassie").bark
    // Creation of new instance is automatically done by the compiler

  //organize
    // LOCAL scope
  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted  // List(3, 2, 1)

  // IMPORTED scope
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    println("Hello, Future")
  }

  //Fetches in this order: local scope -> imported scope

  // Companion Objects of types included in the call
  object Person {
    implicit val personOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  List(Person("Bob"), Person("Alice")).sorted
    // Calls the implicit personOrdering value
}
