package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChildActors.CreditCard.{AttachToAccount, CheckStatus}

object ChildActors extends App {

  // Actors can create other actors

  /*
    CHILD ACTORS
      - When one (parent) actor creates another actor

      Ex:
        context.actorOf(Props[MyActor], "child")
  */

  object Parent {
    //Two messages: One to CreateChild and another to TellChild
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }
  //----------------------------------------------------------------------------
  //ACTOR: Parent
  class Parent extends Actor {
    import Parent._

    //Override receive method for messages, as always
    override def receive: Receive = {
      //Message handler to CREATECHILD
      case CreateChild(name) =>
        println(s"${self.path} creating child")

        // create a new actor (as an actorRef) right HERE using context.actorOf
        val childRef = context.actorOf(Props[Child], name)
        context.become(withChild(childRef))
    }

    //Helper method that sends a message (tells it) to child
    def withChild(childRef: ActorRef): Receive = {
      case TellChild(message) =>
        if(childRef != null) childRef forward message
    }
  }
//----------------------------------------------------------------------------
  //ACTOR: Child
  class Child extends Actor {

    //Override receive method for messages, as always
    override def receive: Receive = {
      case message => println(s"${self.path} I got: $message")
    }
  }

  import Parent._

  //Create new actor system
  val system = ActorSystem("ParentChildDemo")

  //Instantiating an instance of parent actor
  val parent = system.actorOf(Props[Parent], "parent")

  //Parent actor instance sends messages
  parent ! CreateChild("child") //CreateChild message
  parent ! TellChild("hey Kid!") //TellChild message

  // actor hierarchies
  // parent -> child -> grandChild
  //        -> child2 ->

  /*
    Guardian actors (HIGHEST; top-level)
    - /system = system guardian
    - /user = user-level guardian (top-level actor for every single actor we create)
    - / = the root guardian (manages top two)
   */

  /**
   * Actor selection (if need to access something specific)
   */
  val childSelection = system.actorSelection("/user/parent/child2")
  childSelection ! "I found you!"

  /**
   * [!!!] DANGER!
   *
   * NEVER PASS MUTABLE ACTOR STATE, OR THE `THIS` REFERENCE, TO CHILD ACTORS.
   *
   * NEVER IN YOUR LIFE. This breaks actor encapsulation
   */

  //DOMAIN
  object NaiveBankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object InitializeAccount
  }

  class NaiveBankAccount extends Actor {
    import NaiveBankAccount._
    import CreditCard._

    var amount = 0

    override def receive: Receive = {
      case InitializeAccount =>
        val creditCardRef = context.actorOf(Props[CreditCard], "card")
        creditCardRef ! AttachToAccount(this) // !!
      case Deposit(funds) => deposit(funds)
      case Withdraw(funds) => withdraw(funds)

    }

    //Defined methods outside to modularize code
    def deposit(funds: Int) = {
      println(s"${self.path} depositing $funds on top of $amount")
      amount += funds
    }
    def withdraw(funds: Int) = {
      println(s"${self.path} withdrawing $funds from $amount")
      amount -= funds
    }
  }

  //DOMAIN for CreditCard
  object CreditCard {
    //[!!!] VERY DANGEROUS
    case class AttachToAccount(bankAccount: NaiveBankAccount) // !!
    case object CheckStatus
  }

  class CreditCard extends Actor {
    override def receive: Receive = {
      case AttachToAccount(account) => context.become(attachedTo(account))
    }

    def attachedTo(account: NaiveBankAccount): Receive = {
      case CheckStatus =>
        println(s"${self.path} your messasge has been processed.")
        // benign

        account.withdraw(1) // call
          // because I can
          // [!!!] WRONG ON SO MANY LEVELS, as you have a mutable BANK ACCOUNT STATE
          // [!!!] WRONG, because actor state CHANGED without using messages
              // HOW will you effectively debug it?
              // Causes ENORMOUS security issues
              // Just use actor messages to carry this out
          //Using 'this' keyword leaves you vulnerable to message calls from OTHER ACTORS
              // Hence, exposes concurrency issues
    }
  }

  /*
    CLOSING OVER
      - Problems arise (actor subversion, unauthorized access, breaking access)
          > Closing over mutable state
          > 'this' keyword
  */

  import NaiveBankAccount._
  import CreditCard._

  //Testing Naive Wallet
  val bankAccountRef = system.actorOf(Props[NaiveBankAccount], "account")
  bankAccountRef ! InitializeAccount
  bankAccountRef ! Deposit(100)

  Thread.sleep(500)
  val ccSelection = system.actorSelection("/user/account/card")
  ccSelection ! CheckStatus //Withdraws 1 when invoking checkStatus

  // WRONG!!!!!!
}