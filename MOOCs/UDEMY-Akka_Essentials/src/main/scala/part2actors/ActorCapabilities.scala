package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ActorCapabilities.BankAccount.Deposit
import part2actors.ActorCapabilities.Person.LiveTheLife

//(!!!) IMPORTANT:
  //implicit final val self = context.self
  //This MUST be a val (IMMUTABLE)

object ActorCapabilities extends App {

  //REMEMBER: Messages happen in ASYNCHRONOUS matter
  class SimpleActor extends Actor {
    // Every actor has a 'Receive' method, override it
    override def receive: Receive = {
      case "Hi!" => sender() ! "Hello, there!"
        // replying to a message
        // context.sender() == sender()
      // MESSAGE: Type string
      case message: String => println(s"[$self] I have received $message")

      // MESSAGE: Type Int
      case number: Int => println(s"[simple actor] I have received a NUMBER: $number")

      // MESSAGE: Special Type
      case SpecialMessage(contents) => println(s"[simple actor] I have received something SPECIAL: $contents")

      // Sending message to yourself
      case SendMessageToYourself(content) => self ! content

      // Case that sends "Hi!" to TARGET ACTOR (outlined by actorRef 'ref')
      case SayHiTo(ref) => ref ! "Hi!" // alice is being passed as the sender

      case WirelessPhoneMessage(content, ref) => ref forward (content + "s")
      // 'forward' keyword keeps the original sender
      // i keep the original sender of the WPM
    }
  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! "hello, actor"

  // 1 - messages can be of any type
  // a) messages must be IMMUTABLE
    // (!!!) WARNING: No way to check if message is immutable at compile time
  // b) messages must be SERIALIZABLE
  // (!!!) In practice use case classes and case objects

  simpleActor ! 42 // who is the sender?!

  // Defined your OWN message type (note that contents is of type String)
  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("some special content")
//-------------------------------------------------------------------------------------
  // 2 - actors have information about their context and about themselves
  // context.self === `this` in OOP

  /*
    ACTOR CONTEXT
      - Complex data structure with info on environment it runs on
      - Has access to own actor reference
      - actor.self === this (in scala)
      - Can use this to send a message to OURSELVES in Akka
  */

  // Uses
  case class SendMessageToYourself(content: String)
  simpleActor ! SendMessageToYourself("I am an actor and I am proud of it")

  //-------------------------------------------------------------------------------------

  // 3 - actors can REPLY to messages (messages are asynchronous)
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  //This case class takes parameter of ActorRef (remember, this is how you STORE actors)
  case class SayHiTo(ref: ActorRef)
  alice ! SayHiTo(bob)
    //First, alice invokes SayHiTo method to 'bob' actorRef
    //Second, SayHiTo case relays 'Hi!' to 'bob'
    //Third, "Hi!" case gets relayed to bob
    //Fourth, in response, "Hello There!" gets relayed to sender actorRef

  //-------------------------------------------------------------------------------------

  // 4 - dead letters
  alice ! "Hi!" // reply to "me"
    //Dead Letters is an ACTOR STAND-IN when you can't deliver to anyone
  //-------------------------------------------------------------------------------------

  // 5 - forwarding messages (Actors can forward messages to each other)
  // D -> A -> B
  // forwarding = sending a message with the ORIGINAL sender

  case class WirelessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirelessPhoneMessage("Hi", bob) // noSender.

  // 'I have received' is a standard output log from an actorRef

  /**
   * Exercises
   *
   * 1. a Counter actor
   *   - Increment
   *   - Decrement
   *   - Print
   *
   * 2. a Bank account as an actor
   *   receives
   *   - Deposit an amount
   *   - Withdraw an amount
   *   - Statement
   *   replies with
   *   - Success
   *   - Failure
   *
   *   interact with some other kind of actor
   */

  // DOMAIN of the counter
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    import Counter._

    var count = 0

    override def receive: Receive = {
      case Increment => count += 1
      case Decrement => count -= 1
      case Print => println(s"[counter] My current count is $count")
    }
  }

  import Counter._
  val counter = system.actorOf(Props[Counter], "myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print


  // bank account
  object BankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statement

    case class TransactionSuccess(message: String)
    case class TransactionFailure(reason: String)
  }

  class BankAccount extends Actor {
    import BankAccount._

    var funds = 0

    override def receive: Receive = {
      case Deposit(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid deposit amount")
        else {
          funds += amount
          sender() ! TransactionSuccess(s"successfully deposited $amount")
        }
      case Withdraw(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid withdraw amount")
        else if (amount > funds) sender() ! TransactionFailure("insufficient funds")
        else {
          funds -= amount
          sender() ! TransactionSuccess(s"successfully withdrew $amount")
        }
      case Statement => sender() ! s"Your balance is $funds"
    }
  }

  object Person {
    case class LiveTheLife(account: ActorRef)
  }

  class Person extends Actor {
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case LiveTheLife(account) =>
        account ! Deposit(10000)
        account ! Withdraw(90000)
        account ! Withdraw(500)
        account ! Statement
      case message => println(message.toString)
    }
  }

  val account = system.actorOf(Props[BankAccount], "bankAccount")
  val person = system.actorOf(Props[Person], "billionaire")

  person ! LiveTheLife(account)

}