package part2actors

import akka.actor.Actor
import part2actors.ChildActorsExerciseOYO.WordCounterMaster.{WordCountReply, WordCountTask}

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
    // Can have your cases here
    case class Initialize(nChildren: Int)
    case class WordCountTask(text: String, someWorker: WordCounterWorker)
    case class WordCountReply(/* TODO */ count: Int)
  }
  //------------------------------------------------------------------------------------------
  //ACTOR: WordCounterMaster. Receives command, federates it to one of the word count workers
  class WordCounterMaster extends Actor {
    import WordCounterMaster._ //import the domain to use it

    // Implement the actual actors messaging logic here
    override def receive: Receive = {


      case WordCountReply(count) => ???
    }
  }

  // ITC: Imported from the WordCounterWorker
  /*Possibly need DOMAIN: WordCounterWorker
  object WordCounterWorker {
    ???
  }*/
  //------------------------------------------------------------------------------------------
  //ACTOR: WordCounterWorker
  class WordCounterWorker extends Actor {
    import WordCounterMaster._

    override def receive: Receive = {
      // Delimiter is the " " to enable you to count using the .length method
      case WordCountTask(text, someWorker) => sender() ! WordCountReply(text.split(" ").length)
    }
  }
}
