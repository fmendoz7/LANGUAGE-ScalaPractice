package week05

object Exprs {
  def show(e: Expr) = e match {
    case Number(x) => x.toString

    case Sum(l, r) => show(l) + " + " + show(r)
  }

  show(Sum(Number(1), Number(44)))
}

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