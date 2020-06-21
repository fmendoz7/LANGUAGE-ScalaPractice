/*
import week03.Rational
  //Imports Rational class from week03
import week03.{Rational, Holistic}
  //Imports these classes from week03
import week03._
  //Imports everything from week03 package
 */

object scratch {

}

/*
  TRAITS
    - Like in Java, every class can only have one superclass
    - What if you want to inherit from several supertypes?
    - Use traits

    PROPERTIES
      > Declared like an abstract class, just with 'trait' instead of abstract class

      trait Planar {
        def height: Int
        def width: Int
        def surface = height * width
      }
*/