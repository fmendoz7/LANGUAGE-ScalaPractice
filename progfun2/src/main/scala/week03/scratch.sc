/*
import week03.Rational
  //Imports Rational class from week03
import week03.{Rational, Holistic}
  //Imports these classes from week03
import week03._
  //Imports everything from week03 package
import week04
  //This IS NOT a valid statement
 */

import week03.Rational
  //Apparently, if I listed Rationals class under package object, I'm good

object scratch {
  new Rational(1,2)

  def error(msg: String) = throw new Error(msg)

  val x = null
  val y: String = x

  if(true) 1 else false
}

/*
  TRAITS
    - Like in Java, every class can only have one superclass
    - What if you want to inherit from several supertypes?
    - Use traits

    PROPERTIES
      > Declared like an abstract class, just with 'trait' instead of abstract class
      > More powerful than abstract classes because they have concrete methods
      > Traits CANNOT have any parameters of their own. Only classes can.
      > Can have as many traits as you want

    HIGHLY RECOMMEND you look at Scala's class hierarchy at
    http://allaboutscala.com/tutorials/chapter-2-learning-basics-scala-programming/scala-tutorial-learn-scala-class-type-hierarchy/

      trait Planar {
        def height: Int
        def width: Int
        def surface = height * width
      }

      class Square extends Shape with Planar with Movable
*/

/*
  EXCEPTION HANDLING
    - Very similar to Java
    - Uses keyword 'throw'

    throw Exc
      //aborts evaluation with exception Exc
      //Type of this expression is Nothing
*/