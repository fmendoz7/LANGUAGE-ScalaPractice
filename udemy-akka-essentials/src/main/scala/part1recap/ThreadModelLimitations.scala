package part1recap
import scala.concurrent.{Future, future}

object ThreadModelLimitations extends App{
  //DR #1: OOP Encapsulation is only valid in the SINGLE THREADED MODEL
  class BankAccount(private var amount: Int) {
    //Override standard toString method
    override def toString: String = "" + amount

    //Setter method
    def withdraw(money: Int) = this.synchronized {
      this.amount -= money
    }

    //Getter method
    def getAmount = amount
  }

  //  val account = new BankAccount(2000)
  //  for(_ <- 1 to 1000) {
  //    new Thread(() => account.withdraw(1)).start()
  //  }
  //
  //  for(_ <- 1 to 1000) {
  //    new Thread(() => account.deposit(1)).start()
  //  }
  //  println(account.getAmount)

  // OOP encapsulation is broken in a multithreaded env
  // synchronization! Locks to the rescue

  // deadlocks, livelocks

  //DR #2: Delegating something to a thread is a PAIN
    //Ex: You have a running thread. want to pass a runnable to that thread

  var task: Runnable = null
  val runningThread: Thread = new Thread(() => {
    while(true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task!")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) task = r
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println(42))
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("this should run in the background"))

  // DR #3: Tracing & Dealing with errors in multithreaded environment is a pain
    //Ex: 1 million numbers shared between 10 threads (100,000 each)
  import scala.concurrent.ExecutionContext.Implicits.global

  val futures = (0 to 9)
    .map(i => 10000 * i until 100000 * (i + 1)) //0 - 99999, 100000 - 199999, etc
    .map(range => Future {
      if (range.contains(546735)) throw new RuntimeException("Invalid Number")
      range.sum
    })

    val sumFuture = Future.reduceLeft(futures)(_ + _) //Future is sum of all the numbers
    sumFuture.onComplete(println)
}
