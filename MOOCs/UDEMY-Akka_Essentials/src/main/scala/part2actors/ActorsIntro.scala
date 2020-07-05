package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {

  // part1 - actor systems
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  /*
    ACTORS
      - Heavyweight data structure that have many threads under the hood
      - Threads allocate to running actors
      - Actors can only have alphanumeric characters, no spaces, no hyphens

      - Actors are uniquely identified
      - Actors are FULLY ENCAPSULATED, you can't 'take their brain'
      - Messages are all asynchronous
  */
//-------------------------------------------------------------------------------------------
  // part2 - create actors

  // Declared ACTOR, WordCountActor
  class WordCountActor extends Actor {
    // internal data
    var totalWords = 0

    // behavior
      //ITC: They are parsing a string and counting the number of words
    def receive: Receive = {

      //If message was a string, perform these actions
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length

      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }
  //-------------------------------------------------------------------------------------------

  /*
    INSTANTIATING ACTORS
      - Cannot make a new one by invoking a keyword (ie: new)
      - Can make a new one by utilizing Actor system
  */

  // part3 - instantiate our actor
    // FORMAT
    // val <actorReference> = actorSystem.actorOf(Props[<actor>], "<actorName>")
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  //-------------------------------------------------------------------------------------------

  // part4 - communicate!
    // FORMAT (to send messages to actors)
    // <actorReference> ! "some message"
    // <actorReference>.!("some message")
  wordCounter ! "I am learning Akka and it's pretty damn cool!" // "tell"
  anotherWordCounter ! "A different message"
  // asynchronous!

  /*
    ACTOR ENCAPSULATION PROPERTY
      - Remember, actors are FULLY ENCAPSULATED. You cannot access them directly.
      - You can only SEND MESSAGES to them.
   */

  object Person {
    def props(name: String) = Props(new Person(name))
  }

  //(???): How would you instantiate actors with constructor arguments
    //SOLUTION: You use props with an argument
  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  //Used as .props, with actor as an argument for PERSON class
  val person = actorSystem.actorOf(Person.props("Francis"))
  person ! "hi"
}