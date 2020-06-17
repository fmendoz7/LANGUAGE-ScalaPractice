object session3 {
  def sqrt(x: Double) = {

    //Why not just use in-built .abs function?
    def abs(x: Double) = if (x < 0) -x else x

    def sqrtIter(guess: Double, x: Double): Double =
      if (isGoodEnough(guess, x)) guess
      else sqrtIter(improve(guess, x), x)

    def isGoodEnough(guess: Double, x: Double) =
    //Delta would never reach that margin of error for extremely large numbers
    //Now we made error proportional to size of number
      if (abs(guess * guess - x) / x < 0.000000001)
        true
      else
        false

    def improve(guess: Double, x: Double) = (guess + x / guess) / 2

    //Now, design is much cleaner
    sqrtIter(1.0, x)
  }
}

session3.sqrt(1e6)