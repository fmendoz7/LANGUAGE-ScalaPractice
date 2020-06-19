//In this adaptation of our rationals class, use infix for more natural processing

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

  //Less Infix method
  def < (that: Rational) = numer * that.denom < that.numer * denom

  //Max method
  def max(that: Rational) =
  //"this" keyword
    if(this < that) that
    else this

  //Add Method
  def + (that: Rational) =
    new Rational(
      //Answer after making same denom
      //"this" keyword implies current object
      this.numer * that.denom + that.numer * this.denom,
      denom * that.denom
    )

  //Neg Method
    //(!!!) IMPORTANT: If using symbols for method names, ONE SPACE between that and colon
    //(!!!) unary_ allows a symbolic infix method name rep to go first
  def unary_- : Rational = new Rational(-numer, denom)

  //Sub Method
  def - (that: Rational) = this + -that

  override def toString = {
    //Important to normalize so you *DO NOT* run into arithmetic overflows
    val g = gcd(this.numer, this.denom)
    this.numer/g + "/" + this.denom/g
  }
}
//------------------------------------------------------------
object rationalsInfix {
  //Created a new instances of class Rational (invoked constructor)
  val x = new Rational(1, 3)
  val y = new Rational(5, 7)
  val z = new Rational(3, 2)

  //Upon invoking Rational, params already assigned
  //All you are doing is just listing parameters
  x.numer
  x.denom
  x - y - z //Reexpressed with infix notation
  y + y     //Reexpressed with infix notation
  x < y     //Reexpressed with infix notation
  x.max(y)  //Reexpressed with infix notation
  new Rational(2)
}
//------------------------------------------------------------
rationalsInfix.x.+(rationalsInfix.y)
rationalsInfix.x + rationalsInfix.y