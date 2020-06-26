package week05
import math.Ordering

object mergesort {
  //Specifying 'implicit' keyword ensures that certain parameter is assumed
  def msort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length / 2
    if(n == 0) xs

    else {
      def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          //Need to generalize operators to accomodate T
          if (ord.lt(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (fst, snd) = xs splitAt n
      //merge(msort(fst)(ord), msort(snd)(ord))

      //Because parameter was made implicit, you can now leave out ord
      merge(msort(fst), msort(snd))
    }
  }

  val nums = List(2, -4, 5, 7, 1)
  val fruits = List("apple", "pineapple", "orange", "banana")
  msort(nums)
  msort(fruits)
}
