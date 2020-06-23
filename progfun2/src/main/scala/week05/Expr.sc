trait Expr {
  //Pattern Matching using eval
  def eval: Int = this match {
      //Definitely possible to define evaluation function as method of base trait
    case Number(n) => n

    case Sum(e1, e2) => e1.eval + e2.eval
  }
}

eval(Sum(Number(1), Number(2)))

/*
  PATTERN MATCHING
    - An extremely important concept in Scala
    - match is followed by a series of CASES
    - Patterns constructed from
        > Constructors (itc: Number, Sum)
        > Variables (itc: n, e1, e2)
        > Wildcard Patterns
        > Constants
    - Format is:
        pat => expr
    - MatchError exception thrown if no pattern matches selector value

    EX: e match {
      case pat1 => expr1
        ...
      case patN => exprN
    }
*/