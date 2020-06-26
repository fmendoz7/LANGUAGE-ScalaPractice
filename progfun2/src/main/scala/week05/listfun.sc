//package week05
  //package declarations apparently not welcome in worksheets

object listfun {
  val nums = List(2, -4, 5, 7, 1)
  val fruits = List("apple", "pineapple", "orange", "banana")

  nums filter (x => x > 0)
  nums filterNot (x => x > 0)
  nums partition (x => x > 0)

  nums takeWhile (x => x > 0)
  nums dropWhile (x => x > 0)
  nums span (x => x > 0)

  val data = List("a", "a", "a", "b", "c", "c", "a")

  def pack[T](xs: List[T]): List[List[T]] = xs match {
    case Nil => Nil
    case x :: xs1 =>
      val (first, rest) = xs span (y => y == x)
      first :: pack(rest)
  }

  pack(data)

  def encode[T](xs: List[T]): List[(T, Int)] =
    pack(xs) map (ys => (ys.head, ys.length))

  encode(data)
}

/*
  SCALA MAPS
    - Maps are the hashmap/dictionary equivalent in scala
    - Standard (key, value) pair

    EX: These two are the same
      Map("x" -> 24, "y" -> 25, "z" -> 26)
      Map(("x", 24), ("y", 25), ("z", 26))
*/

//Can combine elements of a list. ITC, we do it recursively
def sum(xs: List[Int]): Int = xs match {
  case Nil => 0
  case y :: ys => y + sum(ys)
}

/*
  REDUCELEFT: Inserts a given binary operator between adjacent list elements
    EX:
      List(x1, ..., xn) reduceLeft op == (...(x1 op x2) op ...) op xn

    EX:
      def sum(xs: List[Int]) = (0 :: xs) reduceLeft ((x, y) => x + y)
      def product(xs: List[Int]) = (1 :: xs) reduceLeft ((x, y) => x * y)

    - NOTE: You can write shorter by using underscores
    - "_" represents a new parameter, going from left to right

    EX:
      def sum(xs: List[Int]) = (0 :: xs) reduceLeft (_ + _)
      def product(xs: List[Int]) = (1 :: xs) reduceLeft (_ * _)

*/

/*
  FOLDLEFT: Like a reduceLeft but takes an accumulator, z, as an additional
  parameter, which is returned when foldLeft is called on an empty list

    EX:
      def sum(xs: List[Int]) = (xs foldLeft 0)(_ + _)
      def product(xs: List[Int]) = (xs foldLeft 1)(_ * _)
*/


//(!!!) IMPORTANT: foldLeft and reduceLeft unfold on trees LEANING TO THE LEFT
abstract class List[T] {
  def reduceLeft(op: (T, T) => T): T = this match {
    case Nil => throw new Error("Nil.reduceLeft")
    case x :: xs => (xs foldLeft x)(op)
  }

  def foldLeft[U](z: U)(op: (U, T) => U): U = this match {
    case Nil => z
    case x :: xs => (xs foldLeft op(z, x))(op)
  }

  def reduceRight(op: (T,T) => T): T = this match {
    case Nil => throw new Error("Nil.reduceRight")
    case x :: Nil => x
    case x :: xs => op(x, xs.reduceRight(op))
  }

  def foldRight[U](z: U)(op: (T, U) => U): U = this match {
    case Nil => z
    case x :: xs => op(x, (xs foldRight z)(op))
  }
}

/*
  KNOWING WHICH ONE TO USE

  EX:
    def concat[T](xs: List[T], ys: List[T]): List[T] =
      (xs foldRight ys)(_ :: _)

  TREE:
          ::
        x1  ::
          x2  ::
            xm  ::
              y1  ::
                      ...
                          ::
                        yn  Nil
*/

