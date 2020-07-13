package part2actors

import akka.actor.Actor
import part2actors.ChildActorsExerciseOYO.WordCounterMaster.WordCountTask

/*
  create WordCounterMaster
    - [~] send Initialize(10) to wordCounterMaster
    - send "Akka is awesome" to wordCounterMaster
        > sends WordCountTask("...") to one of its children
          > child replies with WordCountReply(3) to its master
        > master replies with (<itc>, 3) to the sender

    requester -> wcm -> wcw
            r <- wcm <-

    ROUND ROBIN LOGIC:
      - Queue tasks and actors linearly
      Ex: 5 nodes (1,2,3,4,5) and 7 tasks
        order should be 1,2,3,4,5,1,2
*/

object ChildActorsExerciseOYO extends App{
  //Distributed Word Counting

  //DOMAIN: WordCounterMaster
  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(text: String, someWorker: WordCounterWorker)
    case class WordCountReply(/* TODO */ count: Int)
  }

  //ACTOR: WordCounterMaster
  class WordCounterMaster extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case Initialize(nChildren) =>
          println(s"INITIALIZING: $nChildren")

        //[!!!] For loop to create n references for each
          //Or have it represented as a LIST
        //Create actorRef of child actor
        name = s"WCWRef$n" //n is some sort of counter
        val WCWRef = context.actorOf(Props[WordCounterWorker], name)
        context.become()

      case WordCountTask(text, someWorker) =>


      case WordCountReply(count) => ???
    }
  }

  //Possibly need DOMAIN: WordCounterWorker
  object WordCounterWorker {
    ???
  }

  //ACTOR: WordCounterWorker
  class WordCounterWorker extends Actor {
    override def receive: Receive = {
      ???
    }
  }
}
