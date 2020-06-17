object session2 {
  println(1 + 3)

  //Listing Helper Functions
  def sqrtIter(guess: Double, x: Double): Double =
    if(isGoodEnough(guess, x)) guess
    else(sqrtIter(improve(guess,x), x))

  def isGoodEnough(guess: Double, x: Double) =
    //In scala, absolute value method is .abs
    //Determine if margin for error is less than 0.001

    //Can change accuracy by changing margin for error
    if((guess * guess - x).abs / x < 0.00000001) true
    else false

  def improve(guess: Double, x: Double) =
    //Sheer number of iterations for large numbers and opposite order of mag to get across
    //Can eliminate nontermination for large numbers by using
    (guess + x / guess) / 2

  def sqrt(x: Double) = sqrtIter(1.0, x)
}

session2.sqrt(2)
session2.sqrt(4)
session2.sqrt(16)
session2.sqrt(1e-9)
session2.sqrt(1e6)