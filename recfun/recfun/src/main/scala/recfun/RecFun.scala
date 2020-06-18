package recfun

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

  /**
   * Exercise 1
   */
  //(!!!) YOU DID need tail recursion. Constructing traingle
  def pascal(c: Int, r: Int): Int =
    //Do not need tail recursion here
      //#1: Base Case as you whittle down to 0,0 base case
        //r == 0 is redundant, as only the top element is covered. Covered by c == r
      if(c == 0 || c == r || r ==0 ) 1

      //#2: Handle the EDGES
      else pascal(c, r-1) + pascal(c-1, r-1)

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = ???
    //Leetcode similarity. Use a stack.

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = ???
}
