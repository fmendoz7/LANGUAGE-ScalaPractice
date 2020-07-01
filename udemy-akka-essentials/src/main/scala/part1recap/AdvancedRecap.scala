package part1recap

import scala.concurrent.Future

// extends App ensures code is runnable
object AdvancedRecap extends App {
  //PARTIAL FUNCTIONS
    //Functions only runnable on a subset set of input domain
    //Does not provide an answer for any possible input value it could be given
    //Hence, provides an answer only for a subset of possible data, defining data IT CAN handle
  val partialFunction: PartialFunction[Int, Int] = {
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

  //LIFTING
  val lifted = partialFunction.lift
    //total function Int => Option[Int]
  lifted(2)
  lifted(5000)

  //orElse
  val pfChain = partialFunction.orElse[Int, Int] {
    case 60 => 9000
  }

  pfChain(5)    //999 per partialFunction
  pfChain(60)   // 9000
  pfChain(457)  //throws a MatchError


  //TYPE ALIASES
  type ReceiveFunction = PartialFunction[Any, Unit]

  def receive: ReceiveFunction = {
    case 1 => println("hello")
    case _ => println("Anomalous Input Detected")
  }

  //IMPLICITS
  //Implicit Parameters
      // -
  implicit val timeout = 3000
  def setTimeout(f: () => Unit)(implicit timeout: Int) = f()

  setTimeout(() => println("timeout"))  //extra parameter list omitted

  //Implicit Conversions
    //1) Implicit defs
  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(string: String): Person = Person(string)
  "Peter".greet
    // fromStringToPerson("Peter").greet
    // This type conversion is done automatically by the compiler

  //Implicit Classes
  implicit class Dog(name: String) {
    def bark = println("bark!")
  }
  "Lassie".bark
    // new Dog("Lassie").bark
    // Creation of new instance is automatically done by the compiler

  //organize
  //local scope
  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted  // List(3, 2, 1)

  // imported scope
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    println("Hello, Future")
  }

  // Companion Objects of types included in the call
  object Person {
    implicit val personOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  List(Person("Bob"), Person("Alice")).sorted

}
