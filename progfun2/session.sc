import java.util
object session {
  val arrayList = new util.ArrayList[Int]()

  for (i <- 1 to 10) {
    //println is NOT reflected within the REPL
    println("i = " + i)
  }
}

//In this case, I make an actual call to the object
session