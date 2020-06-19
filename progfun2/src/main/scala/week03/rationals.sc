class Rational(x: Int, y: Int) {
  //Properties of a rational number, having numerator & denominator

  //Require enforces for preconditions (throws IllegalArgument error)
  require(y != 0, "ERROR: Denominator must be nonzero!")

  //Assert checks the call of the function itself (throws AssertionError)
  //assert(x >= 0)

  //Alternative Constructor (constructor overloading, itc using this)
  def this(x: Int) = this(x, 1)

  //Private: accessible within the class only
  private def gcd(a: Int, b: Int): Int = if(b == 0) a else gcd(b, a % b)

  //Simplified numer and denom
  def numer = x
  def denom = y

  //Less method
  def less(that: Rational) = numer * that.denom < that.numer * denom

  //Max method
  def max(that: Rational) =
    //"this" keyword
    if(this.less(that)) that
    else this

  //Add Method
  def add(that: Rational) =
    new Rational(
      //Answer after making same denom
      //"this" keyword implies current object
      this.numer * that.denom + that.numer * this.denom,
      denom * that.denom
    )

  //Neg Method
  def neg(that: Rational) = new Rational(-numer, denom)

  //Sub Method
  def sub(that: Rational) = add(neg(that))

  override def toString = {
    val g = gcd(this.numer, this.denom)
    this.numer/g + "/" + this.denom/g
  }
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
  x.sub(y).sub(z)
  y.add(y)
  x.less(y)
  x.max(y)
  new Rational(2)
}
//------------------------------------------------------------
rationals.x.add(rationals.y)