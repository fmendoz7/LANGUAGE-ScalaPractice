object productCurrying {
  //Remember: In higher order functions, only spec types
  //Fill out exact implementation later
  def product(f: Int => Int)(a: Int, b: Int): Int =
    if(a > b) 1
    else f(a) * product(f)(a+1, b)

  def fact(n: Int) = product(x => x)(1, n)

  def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero:Int)(a: Int, b: Int): Int =
    if(a > b) zero
    else combine(f(a), mapReduce(f, combine, zero)(a + 1, b))
}

productCurrying.product(x => x * x)(3,4)
productCurrying.fact(3)

//b is the number in question