package part1recap
import scala.concurrent.{Future, future}

object ThreadModelLimitations extends App{

  //DR #1: OOP Encapsulation is only valid in the SINGLE THREADED MODEL
    //Ex: Unsafe Bank Account
  class BankAccount(private var amount: Int) {
    //Override standard toString method
    override def toString: String = "" + amount

    //THREAD-SAFE withdraw
    def withdraw(money: Int) = this.synchronized {
      this.amount -= money
    }

    //THREAD-SAFE deposit
    def deposit(money: Int) = this.synchronized {
      this.amount += money
    }

    //Getter methods don't need sync
    def getAmount = amount
  }

  /*
    val account = new BankAccount(2000)
    //Create 1000 threads withdrawing $1.00
    for(_ <- 1 to 1000) {
        new Thread(() => account.withdraw(1)).start()
    }

    //Create 1000 threads depositing $1.00
    for(_ <- 1 to 1000) {
        new Thread(() => account.deposit(1)).start()
    }
    println(account.getAmount)
  */

  // OOP encapsulation is broken in a multithreaded env synchronization! Locks to the rescue

  // PROBLEM: Deadlocks, Livelocks, etc. would arise
  // SOLUTION: Need a Data Structure that is FULLY ENCAPSULATED with NO LOCKS

//------------------------------------------------------------------------------------------------

  //DR #2: Delegating something to a thread is a PAIN
    //NOTE: This is NOT talking about executor services
    //Ex: You have a running thread. want to pass a runnable to that thread

  var task: Runnable = null
  val runningThread: Thread = new Thread(() => {
    while(true) {
      while (task == null) {
        runningThread.synchronized {
          //Waiting to be assigned to a runnable
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }

      //Moment the task DOESN'T become null
      task.synchronized {
        println("[background] I have a task!")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) task = r

    //THREAD-SAFE notify method
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println(42)) //Executed thread in question
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("this should run in the background"))

  /*
    PROBLEM: What if you want to run
      - Other signals?
      - Multiple background tasks and threads?
      - Know who gave the signal?
      - What if you crash?
  */

  /*
    SOLUTION: Need a data structure that
      - Can safely receive messages
      - Identify sender
      - Easily identifiable
      - Can guard against errors
  */

  //------------------------------------------------------------------------------------------------

  // DR #3: Tracing & Dealing with errors in multithreaded environment is a PAIN
    //Ex: Compute sum of 1 million numbers shared between 10 threads (100,000 each)
  import scala.concurrent.ExecutionContext.Implicits.global

  val futures = (0 to 9)
    .map(i => 10000 * i until 100000 * (i + 1)) //0 - 99999, 100000 - 199999, etc
    .map(range => Future {
      //Representing the future failing with some obscure error
      if (range.contains(546735)) throw new RuntimeException("Invalid Number")
      range.sum
    })

    val sumFuture = Future.reduceLeft(futures)(_ + _) //Future is sum of all the numbers
    sumFuture.onComplete(println)

    //WARNING: Bug Hunting in a GIANT DISTRIBUTED APPLICATION is an absolute pain in the neck
}

/*
  THREAD MODEL LIMITATIONS
    - OOP is not encapsulated
    - Could use locks, but will result in other future problems
        >> Deadlocks
        >> Livelocks
        >> Headaches
    - Massive pain to solve in distributed environments
    - Delegating tasks are a pain in the neck
        >> Hard and error-prone to debug
        >> Never feels "first-class" although often needed
        >> Should never be done in a blocking fashion
    - Dealing with error
        >> Monumental task even in small systems
*/