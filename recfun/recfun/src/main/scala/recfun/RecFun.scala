package recfun
import scala.collection.mutable.Stack

object RecFun extends RecFunInterface {

  def main(args: Array[String]): Unit = {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        //Column, row
        print(s"${pascal(col, row)} ")
      println()
    }
  }
  //-----------------------------------------------------------------------------------------
  /** Exercise 1 */
  //(!!!) YOU DID need tail recursion. Constructing traingle
  def pascal(c: Int, r: Int): Int =
      //#1: Base Case as you whittle down to 0,0 base case
        //r == 0 is redundant, as only the top element is covered. Covered by c == r
      if(c == 0 || c == r || r ==0 ) 1

      //#2: Handle the EDGES
      else pascal(c, r-1) + pascal(c-1, r-1)
  //-----------------------------------------------------------------------------------------
  /** Exercise 2 */
  def balance(chars: List[Char]): Boolean = {
    //YOU CANNOT have mutable local variables in Scala
    //So everything has to be defined in the parameter..

    //Defining recursive helper method
    def calculateCounter(chars: List[Char], decidingCounter: Int): Boolean = {
      if(chars.isEmpty == true && decidingCounter == 0) true
      else if (chars.isEmpty == true && decidingCounter != 0) false
      else {
        //So in recursion, esp with lists, iterate via HEAD and TAIL
        if (chars.head == '(') {
          decidingCounter + 1
        }

        else if (chars.head == ')') {
          decidingCounter - 1
        }
      }

      //This statement prevents overstepping one location in memory
      var listCheck = chars.tail
      if(listCheck.isEmpty == false) {
        calculateCounter(chars.tail, decidingCounter)
      }
    }

    if (calculateCounter(chars, 0) == true) true
    else false
  }
  //-----------------------------------------------------------------------------------------
  /** Exercise 3 */
  def countChange(money: Int, coins: List[Int]): Int = {
    //Using tail recursion to iterate through different heads and tails of lists

    def recursiveCounter(reCount: Int, attemptList: List[Int]): Int = {
      if (reCount == money) 1
      else if (reCount > money || attemptList.isEmpty) 0
      else recursiveCounter(reCount + attemptList.head, attemptList) + recursiveCounter(reCount, attemptList.tail)
    }

    if (money == 0) 0
    else recursiveCounter(0, coins)
  }
}
