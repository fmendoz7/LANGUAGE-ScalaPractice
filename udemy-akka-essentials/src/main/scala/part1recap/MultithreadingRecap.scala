package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MultithreadingRecap extends App {
  /* SCALA MULTITHREADING (https://www.geeksforgeeks.org/scala-multithreading/)
      - To use multithreading in Scala, you use a Thread constructor
      - Ensure creating safe threads by either using
          >> Synchronous Block
          >> @volatile keyword (limited to PRIMITIVE data types)
  */

  val aThread = new Thread(() => println("I'm running in parallel"))
  aThread.start() // start a thread
  aThread.join()  // wait for a thread to finish

  //PROBLEM #1: Threads are unpredictable
  val threadHello = new Thread(() => (1 to 1000).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 1000).foreach(_ => println("goodbye")))

  //Example: Run these threads in parallel
    //OBSERVATION: Erratic mix of 'hellos' and 'goodbyes'
  threadHello.start()
  threadGoodbye.start()

  //PROBLEM #2: Different runs produce different results!
  class BankAccount(@volatile private var amount: Int) {
    //@volatile keyword solution for unsafe threads, only around PRIMITIVE data types
    override def toString: String = "" + amount

    //(!!!) WARNING: Method described is NOT THREAD-SAFE
      //Explanation: No two threads can run -= operation at the same time
    def withdraw(money: Int) = this.amount -= money

    // This method IS THREAD-SAFE
      //Explanation: this.synchronized{} blocks are around critical logic
    def safeWithdraw(money: Int) = this.synchronized {
      this.amount -= money
    }
  }

  /*
    EXAMPLE OF MULTITHREADING PROBLEM
     BA (10000) //10,000 starting balance

     T1 -> withdraw 1000
     T2 -> withdraw 2000
     T1 -> this.amount = this.amount - .... // PREEMPTED by the OS
     T2 -> this.amount = this.amount - 2000 = 8000

     T1 -> -1000 = 9000
     => result = 9000

     this.amount = this.amount - 1000 is NOT ATOMIC
      //ATOMIC OPERATIONS: Thread Safe
      //This operation is NOT thread safe (no two threads can execute this at the same time)

      //SOLUTION #1: Use synchronized blocks around critical instruction
      //SOLUTION #2: Use @volatile type around PRIMITIVE data types
  */
//----------------------------------------------------------------------------------------------------
  // inter-thread communication on the JVM
  // wait - notify mechanism

  /* SCALA FUTURES (https://docs.scala-lang.org/overviews/scala-book/futures.html)
      - Placeholder object for value that may/may not exist yet, but will be available at some point
      - Alternatively, it will throw an exception for a value that could NOT be made available
      - Temporary pocket of concurrency for one-shot needs

      Ex: Regular Assignment
        def aShortRunningTask(): Int = 42
        val x = aShortRunningTask

      Ex: Same thing using Scala Futures
        def aLongRunningTask(): Future[Int] = ???
        val x = aLongRunningTask
          //Even though not available yet, assignment is ready
  */

  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    //long computation- on a different
    //ITC, this thread will just return 42
    42
  }

  /* CALLBACK FUNCTIONS (https://developer.mozilla.org/en-US/docs/Glossary/Callback_function)
      - Callback Function: A function passed into another function as an argument
      - Function is then invoked inside the outer function to complete some routine or action
      - Callbacks can be SYNCHRONOUS, but more often than not, they are ASYNCHRONOUS
  */

  //FUTURES APPLICATION #1: Callbacks
  future.onComplete {
    //Similar to Javascript
    case Success(42) => println("SUCCESS: I found the meaning of life")
    case Failure(_) => println("ERROR: Something happened with the meaning of life!")
  }

  val aProcessedFuture = future.map(_ + 1) // Returns future == 43
  val aFlatFuture = future.flatMap {
    // Run another future inside
    value => Future(value + 2)
  } // Returns future == 44

  val filteredFuture = future.filter(_ % 2 == 0)
    // If passes predicate, returned future is identical to original future
    // Otherwise, throws NoSuchElementException

  //for comprehensions
  val aNonsenseFuture = for {
    meaningOfLife <- future
    filteredMeaning <- filteredFuture
  } yield meaningOfLife + filteredMeaning

  // Other utilities available for futures
    // andThen, recover/recoverWith
    // Promises
}
