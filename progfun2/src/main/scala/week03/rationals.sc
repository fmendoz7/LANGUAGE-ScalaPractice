class Rational(x: Int, y: Int) {
  //Properties of a rational number, having numerator & denominator
  def numer = x
  def denom = y

  //Add Method
  def add(that: Rational) =
    new Rational(
      //Answer after making same denom
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

  //Neg Method
  def neg(that: Rational) = new Rational(-numer, denom)

  //Sub Method
  def sub(that: Rational) = add(neg(that))

  override def toString = numer + "/" + denom
}
//------------------------------------------------------------
object rationals {
  //Created a new instances of class Rational (invoked constructor)
  val x = new Rational(1, 3)
  val y = new Rational(5, 7)
  val z = new Rational(3, 2)

  //Upon invoking Rational, params already assigned
  //All you are doing is just listing parameters
  x.numer
  x.denom

  x.add(y)
}
//------------------------------------------------------------
rationals.x.add(rationals.y)