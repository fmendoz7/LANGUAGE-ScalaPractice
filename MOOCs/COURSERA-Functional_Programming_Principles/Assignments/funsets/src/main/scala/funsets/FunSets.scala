package funsets
//(!!!) IMPORTANT: funset class tells you if it is a VALID ELEMENT or NOT
//(!!!) IMPORTANT: Defining the properties of a set by the POTENTIAL VALUES IT COULD INCLUDE
  //This approach, therefore, saves memory

/**
 * 2. Purely Functional Sets.
 */
trait FunSets extends FunSetsInterface {
  /**
   * We represent a set by its characteristic function, i.e.
   * its `contains` predicate.
   */
  override type FunSet = Int => Boolean
  //Means take in parameter: Int and return: Boolean
  //------------------------------------------------------------------------
  /**
   * Indicates whether a set contains a given element.
   */
  def contains(s: FunSet, elem: Int): Boolean = s(elem)
  //------------------------------------------------------------------------
  /** Returns the set of the one given element. */
  def singletonSet(elem: Int): FunSet = (x => x == elem)
  //Basically used an anonymous function (assuming type FunSet)
  //x is an integer (that does WHAT??)

  //------------------------------------------------------------------------
  /**
   * Returns the union of the two given sets,
   * the sets of all elements that are in either `s` or `t`.
   */
  def union(s: FunSet, t: FunSet): FunSet = (x => s(x) || t(x))
  //------------------------------------------------------------------------
  /**
   * Returns the intersection of the two given sets,
   * the set of all elements that are both in `s` and `t`.
   */
  def intersect(s: FunSet, t: FunSet): FunSet = (x => s(x) && t(x))
  //------------------------------------------------------------------------
  /**
   * Returns the difference of the two given sets,
   * the set of all elements of `s` that are not in `t`.
   */
  def diff(s: FunSet, t: FunSet): FunSet = (x => s(x) && !t(x))
  //------------------------------------------------------------------------
  /**
   * Returns the subset of `s` for which `p` holds.
   */
  def filter(s: FunSet, p: Int => Boolean): FunSet = (x => s(x) && p(x))

  //------------------------------------------------------------------------
  /** The bounds for `forall` and `exists` are +/- 1000 */
    //Checking if elements within bound (range) in FunSet is valid within p
  val bound = 1000
  /**
   * Returns whether all bounded integers within `s` satisfy `p`.
   */

    //(!!!) IMPORTANT: You MUST define function p logic when calling method
    //(!!!) REMEMBER: In Scala, abstract types in parameters for higher order functions
      //When making method call, you must write out explicit anonymous higher-order function detail
  def forall(s: FunSet, p: Int => Boolean): Boolean = {
    //Element a, assessing if part of (valid for) FunSet
    def iter(a: Int): Boolean = {
      //Base case
      if (s(a) && !p(a)) false
      else if (a > bound) true
        //(??) Implicate stack trace until 1000?
      else iter(a + 1)
    }

    //Recursive call through iter
      //Essentially, began at lower bound, iterated through to upperbound
    iter(-bound)
  }
  //------------------------------------------------------------------------
  /**
   * Returns whether a bounded integer exists within `s`
   * that satisfies `p`.
   */
  def exists(s: FunSet, p: Int => Boolean): Boolean = !(forall(s, x => !p(x)))
    //Isn't it just better to have x => p(x)?? I see a double negative
    //REMEMBER: Define explicit implementation details within p, an anonymous higher-order function, in call
    //We are calling 's' to represent the whole FunSet, not just get a specific element from it
  //------------------------------------------------------------------------
  /**
   * Returns a set transformed by applying `f` to each element of `s`.
   */
    //Assuming f is already defined, logic would be elaborated when calling it
    //Already assumed that every element of 's' is within the new set.
    //New set 'p' defined explicitly as y => f(y) == x
      //Should it be y == f(x)??
  def map(s: FunSet, f: Int => Int): FunSet = (x => exists(s, y => f(y) == x))
  //------------------------------------------------------------------------
  /**
   * Displays the contents of a set
   */
  def toString(s: FunSet): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }
//------------------------------------------------------------------------
  /**
   * Prints the contents of a set on the console.
   */
  def printSet(s: FunSet): Unit = {
    println(toString(s))
  }
}

object FunSets extends FunSets
