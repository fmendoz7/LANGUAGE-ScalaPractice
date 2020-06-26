abstract class List[T] {
  def filter(p: T => Boolean): List[T] = this match {
    case Nil => this
    case x :: xs => if (p(x)) x :: xs.filter(p) else xs.filter(p)
  }
}

def squareList(xs: List[Int]): List[Int] = xs match {
  case Nil => Nil
  case y :: ys => y*y :: squareList(ys)
}

def squareList(xs: List[Int]): List[Int] = {
  xs map (x => x*x)
}

def posElems1(xs: List[Int]): List[Int] = xs match {
  case Nil => xs
  case y :: ys => if (y > 0) y :: posElems(ys) else posElems(ys)
}

//Can write posElems method (alternative approach) more concisely with filtering
def posElems2(xs: List[Int]): List[Int] = {
  xs filter (x => x > 0)
}

/*
  FILTER VARIATIONS
    - xs filterNot p
    - xs partition p
    - xs takeWhile p
    - xs dropWhile p
    - xs span p
*/