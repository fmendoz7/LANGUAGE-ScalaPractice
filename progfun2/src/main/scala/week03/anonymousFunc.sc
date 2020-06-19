//Ensure proper tailrec library
import scala.annotation.tailrec

object anonymousFunc {
  def sum(f: Int => Int)(a: Int, b: Int): Int = {
    @tailrec
    def loop(a: Int, acc: Int): Int = {
      //Base Case
      if(a > b) acc
        //Increment by 1 so bound accelerates, add a to acc
      else loop(a + 1, acc + f(a))
    }

    //Invoke loop recursively
    loop(a, 0)
  }
}

anonymousFunc.sum(x => x)(1, 10)
anonymousFunc.sum(x => x)(5, 10)
anonymousFunc.sum(x => x)(10, 20)

//acc == Running Total
//a == Lower bound
//b == Upper bound