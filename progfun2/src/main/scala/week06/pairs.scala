package week06

//Find all pairs of positive integers i & j, with:
  // 1 <= j < i < n.
  // i + j is PRIME

object pairs {
  def isPrime(n: Int) = (2 until n).forall(n % _ ! = 0)
    //forall checks if all elements satisfy next spec. property
  val n = 7

  (1 until n) map (i =>
    (1 until i) map (j => (i,j))) filter (pair =>
    isPrime(pair._1 + pair._2))
}

/*
  SCALA FLATMAP
    xs flatmap f = (xs map f).flatten
*/