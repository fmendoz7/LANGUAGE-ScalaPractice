package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChangingActorBehavior.Mom.MomStart

object ChangingActorBehavior extends App {

  // FussyKid DOMAIN
  object FussyKid {
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  // FussyKid actor as a class
  class FussyKid extends Actor {
    import FussyKid._
    import Mom._

    // internal state of the kid
    var state = HAPPY

    //Kid actor changes state depending on food given
    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if (state == HAPPY) sender() ! KidAccept  //Kid plays if happy
        else sender() ! KidReject //Kid does not play if sad
    }
  }
//------------------------------------------------------------------------------------------------------
  //STATELESS implementation of FussyKid
  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    //REMEMBER: Scala Actor message handlers are operating on a STACK mechanism
    def happyReceive: Receive = {
      // (!!!) context.become() allows you to change handlers (if one parameter, implies == True)
          //If second parameter ==False, it means put new handler on a STACK
      case Food(VEGETABLE) => context.become(sadReceive, false)
        // change my receive handler to sadReceive
      case Food(CHOCOLATE) => //Do nothing, satisfied
      case Ask(_) => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false)
      case Food(CHOCOLATE) => context.unbecome()
      case Ask(_) => sender() ! KidReject
    }
  }
  //------------------------------------------------------------------------------------------------------
  // Mom DOMAIN
  object Mom {
    case class MomStart(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String) // do you want to play?
    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  // Mom Actor
  class Mom extends Actor {
    import Mom._
    import FussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        // test our interaction
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("do you want to play?")
      case KidAccept => println("Yay, my kid is happy!")
      case KidReject => println("My kid is sad, but as he's healthy!")
    }
  }

  val system = ActorSystem("changingActorBehaviorDemo")

  //Instantiating new actors based on types defined above
  val fussyKid = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom = system.actorOf(Props[Mom])

  mom ! MomStart(statelessFussyKid)

  /*
    mom receives MomStart
      kid receives Food(veg) -> kid will change the handler to sadReceive
      kid receives Ask(play?) -> kid replies with the sadReceive handler =>
    mom receives KidReject
   */

  /*
    ACTOR MESSAGE HANDLING QUEUE

    context.become
      Food(veg) -> stack.push(sadReceive)
      Food(chocolate) -> stack.push(happyReceive)
      Stack:
        1. happyReceive
        2. sadReceive   //REMEMBER: This is the first call of context.become()
        3. happyReceive //REMEMBER: This was the default state
   */

  /*
    new behavior
    Food(veg)
    Food(veg)
    Food(choco)
    Food(choco)
    Stack:
    1. happyReceive
      //This is because you had 3 happys (1 starting, 2 invoked) and 2 sads (2 invoked), leaving you with 1 happy
   */
  //----------------------------------------------------------------------------------------------------------------------
  // OYO EXERCISE #1 (Replicate as STATELESS counter actor)
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    import Counter._

    //SOLUTION: Incremented generic counting message handler called countReceive
    override def receive: Receive = countReceive(0)
      //countReceive is the go-to, default message handler, with 0 as the starting amount

    //SOLUTION: This is not a message handler, but a method for counting
    def countReceive(currentCount: Int): Receive = {
      //[!!!] THE KEY is to rewrite MUTABLE STATE INTO THE HANDLERS you want to support
          //This is how you turn STATEFUL into STATELESS actors
      //Increment case, modify within context.become
      case Increment =>
        println(s"[$currentCount] incrementing")
        context.become(countReceive(currentCount + 1))

      //Decrement case, modify within context.become - 1
      case Decrement =>
        println(s"[$currentCount] decrementing")
        context.become(countReceive(currentCount - 1))

      case Print => println(s"[counter] my current count is $currentCount")
    }

    //[CORRECT] define as many message handlers as you like
    //[CORRECT] Naively assume will need some variable to keep track
  }

  import Counter._
  val counter = system.actorOf(Props[Counter], "myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print

  // ----------------------------------------------------------------------------------------------------------------------
  // OYO EXERCISE #2
  case class Vote(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReply(candidate: Option[String])

  //Stateful way to represent
  class Citizen extends Actor {
    var candidate: Option[String] = None

    override def receive: Receive = {
      //REM: The way you represent parameters in functions is param1 => param2
      case Vote(c) => candidate = Some(c)
      case VoteStatusRequest => sender() ! VoteStatusReply(candidate)
          //Scenario #1: If you HAVE voted, candidate returned
          //Scenario #2: If you HAVEN'T voted, nothing gets returned
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])
  class VoteAggregator extends Actor {
    //Need to incorporate Set() and Map() for functionality to work
    var stillWaiting: Set[ActorRef] = Set()
    var currentStats: Map[String, Int] = Map()

    override def receive: Receive = {
      case AggregateVotes(citizens) =>
        stillWaiting = citizens
        citizens.foreach(citizenRef => citizenRef ! VoteStatusRequest)
      case VoteStatusReply(None) =>
        //Citizen has not voted yet
        sender() ! VoteStatusRequest //this might end up in an infinite loop

      case VoteStatusReply(Some(candidate)) =>
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
        currentStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
        if (newStillWaiting.isEmpty) {
          println(s"[Aggregator] poll stats: $currentStats")
        } else {
          stillWaiting = newStillWaiting
        }

    }
  }

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

  // ----------------------------------------------------------------------------------------------------------------------

  /**
   * Exercises
   * 1 - recreate the Counter Actor with context.become and NO MUTABLE STATE
   */

//  object Counter {
//    case object Increment
//    case object Decrement
//    case object Print
//  }
//
//  class Counter extends Actor {
//    import Counter._
//
//    override def receive: Receive = countReceive(0)
//
//    def countReceive(currentCount: Int): Receive = {
//      case Increment =>
//        println(s"[countReceive($currentCount)] incrementing")
//        context.become(countReceive(currentCount + 1))
//      case Decrement =>
//        println(s"[countReceive($currentCount)] decrementing")
//        context.become(countReceive(currentCount - 1))
//      case Print => println(s"[countReceive($currentCount)] my current count is $currentCount")
//    }
//  }
//
//  import Counter._
//  val counter = system.actorOf(Props[Counter], "myCounter")
//
//  (1 to 5).foreach(_ => counter ! Increment)
//  (1 to 3).foreach(_ => counter ! Decrement)
//  counter ! Print
//
//  /**
//   * Exercise 2 - a simplified voting system
//   */
//
//  case class Vote(candidate: String)
//  case object VoteStatusRequest
//  case class VoteStatusReply(candidate: Option[String])
//  class Citizen extends Actor {
//    override def receive: Receive = {
//      case Vote(c) => context.become(voted(c))
//      case VoteStatusRequest => sender() ! VoteStatusReply(None)
//    }
//
//    def voted(candidate: String): Receive = {
//      case VoteStatusRequest => sender() ! VoteStatusReply(Some(candidate))
//    }
//  }
//
//  case class AggregateVotes(citizens: Set[ActorRef])
//  class VoteAggregator extends Actor {
//    override def receive: Receive = awaitingCommand
//
//    def awaitingCommand: Receive = {
//      case AggregateVotes(citizens) =>
//        citizens.foreach(citizenRef => citizenRef ! VoteStatusRequest)
//        context.become(awaitingStatuses(citizens, Map()))
//    }
//
//    def awaitingStatuses(stillWaiting: Set[ActorRef], currentStats: Map[String, Int]): Receive = {
//      case VoteStatusReply(None) =>
//        // a citizen hasn't voted yet
//        sender() ! VoteStatusRequest // this might end up in an infinite loop
//      case VoteStatusReply(Some(candidate)) =>
//        val newStillWaiting = stillWaiting - sender()
//        val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
//        val newStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
//        if (newStillWaiting.isEmpty) {
//          println(s"[aggregator] poll stats: $newStats")
//        } else {
//          // still need to process some statuses
//          context.become(awaitingStatuses(newStillWaiting, newStats))
//        }
//    }
//  }
//
//  val alice = system.actorOf(Props[Citizen])
//  val bob = system.actorOf(Props[Citizen])
//  val charlie = system.actorOf(Props[Citizen])
//  val daniel = system.actorOf(Props[Citizen])
//
//  alice ! Vote("Martin")
//  bob ! Vote("Jonas")
//  charlie ! Vote("Roland")
//  daniel ! Vote("Roland")
//
//  val voteAggregator = system.actorOf(Props[VoteAggregator])
//  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

  /*
    Print the status of the votes
    Martin -> 1
    Jonas -> 1
    Roland -> 2
   */
}