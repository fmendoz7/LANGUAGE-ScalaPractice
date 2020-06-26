package week06

object test {
  val xs = Array(1, 2, 3, 44)
  xs map (x => x * 2)

  val s = "Hello World"
  s filter (c => c.isUpper) //Parses relevant elements
  s exists (c => c.isUpper) //Checks if at least one element has property
  s forall (c => c.isUpper) //Checks if property applies to all elements

  val pairs = List(1, 2, 3) zip s
  pairs.unzip

  //Flat map makes a hashmap and separates all characters
    //Combination of 'map' and 'flatten' operations
  s flatMap(c => List('.', c))

  xs.sum
  xs.max

  //APPROACH #1: Compute the scalar product of two vectors
  def scalarProduct1(xs: Vector[Double], ys: Vector[Double]): Double =
    (xs zip ys).map(xy => xy._1 * xy._2).sum

  //APPROACH #2: Compute Scalar Product using PATTERN MATCHING
  def scalarProduct2(xs: Vector[Double], ys: Vector[Double]): Double =
    (xs zip ys).map{case(x,y) => x * y}.sum
}

/*
  VECTORS IN SCALA
    - Created analogously to lists
    - Has more even balanced access patterns than list

    EX:
      val nums = Vector(1, 2, 3, -88)
      val people = Vector("Bob", "James", "Peter")

  VECTOR OPERATIONS (direction of : points to the sequence)
    +:
      > Create new vector with leading element x, followed by all elements of xs
    :+
      > Create new vector with trailing element x, preceded by all elements of xs
*/

/*
  RANGES
    - Represents a sequence of evenly spaced integers
    - Have 3 fields
        > Lower bound
        > Upper bound
        > Step value

    EX:
      val r: Range = 1 until 5
      val s: Range = 1 to 5
      1 to 10 by 3
      6 to 1 by -2
*/

/*
  SEQUENCE OPERATIONS
    - xs exists p
    - xs forall p
    - xs zip ys
    - xs.unzip
    - xs.flatMap f
    - xs.sum
    - xs.product
    - xs.max
    - xs.min
*/