object tailRecursion {
  //Factorial Method
  def factorial(n: Int): Int = {

    //Looping functionality helper method
    def loop(acc: Int, n: Int): Int =
      if (n == 0) acc
      else
        loop(acc * n, n - 1)

      //Execute the function with starting val of 1
      loop(1,n)
  }
}