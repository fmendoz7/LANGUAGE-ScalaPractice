class Rational(x: Int, y: Int) {
  def numer = x
  def denom = y

  def add(that: Rational) =
    new Rational(
      numer * that.denom
    )
}

object rationals {
  //Created a new instance of class Rational
  val x = new Rational(1, 2)

  //Upon invoking Rational, params already assigned
  //All you are doing is just listing parameters
  x.numer
  x.denom
}