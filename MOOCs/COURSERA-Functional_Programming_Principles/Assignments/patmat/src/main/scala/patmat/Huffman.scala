package patmat
import common._

/**
 * A huffman code is represented by a binary tree.
 *
 * Every `Leaf` node of the tree represents one character of the alphabet that the tree can encode.
 * The weight of a `Leaf` is the frequency of appearance of the character.
 *
 * The branches of the huffman tree, the `Fork` nodes, represent a set containing all the characters
 * present in the leaves below it. The weight of a `Fork` node is the sum of the weights of these
 * leaves.
 */
abstract class CodeTree
case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
case class Leaf(char: Char, weight: Int) extends CodeTree

/** Assignment 4: Huffman coding */
//You can think of an interface like a header in C/C++
trait Huffman extends HuffmanInterface {

  // Part 1: Basics (Incorporate Pattern Matching for case classes)
  def weight(tree: CodeTree): Int = tree match {
    case Fork(_,_,_,weight) => weight
    case Leaf(_,weight) => weight
  }

  def chars(tree: CodeTree): List[Char] = tree match {
    case Fork(leftParam, rightParam, chars, _) => chars
    case Leaf(chars, _) => List(chars)
  }

  def makeCodeTree(left: CodeTree, right: CodeTree) =
    //Takes the left and right codetrees, appends the chars of rightCT to leftCT, adds weights of leftCT to rightCT
    Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))
//==================================================================================================
  // Part 2: Generating Huffman trees

  /**
   * In this assignment, we are working with lists of characters. This function allows
   * you to easily create a character list from a given string.
   */
  def string2Chars(str: String): List[Char] = str.toList
    //REMEMBER: CodeTree is of a type with a list of strings

  /**
   * This function computes for each unique character in the list `chars` the number of
   * times it occurs. For example, the invocation
   *
   *   times(List('a', 'b', 'a'))
   *
   * should return the following (the order of the resulting list is not important):
   *
   *   List(('a', 2), ('b', 1))
   *
   * The type `List[(Char, Int)]` denotes a list of pairs, where each pair consists of a
   * character and an integer. Pairs can be constructed easily using parentheses:
   *
   *   val pair: (Char, Int) = ('c', 1)
   *
   * In order to access the two elements of a pair, you can use the accessors `_1` and `_2`:
   *
   *   val theChar = pair._1
   *   val theInt  = pair._2
   *
   * Another way to deconstruct a pair is using pattern matching:
   *
   *   pair match {
   *     case (theChar, theInt) =>
   *       println("character is: "+ theChar)
   *       println("integer is  : "+ theInt)
   *   }
   */
  //Scala PATTERN MATCHING is a more powerful version of the switch statement in Java

  //Should return a List of (Char, Int) tuples to denote count of characters
    //Iterate through the list
  def times(chars: List[Char]): List[(Char, Int)] = {
    chars match {
      case Nil => Nil
      //In scala, case _ is considered a catch-all
      case _ =>
        val ascSorted = chars.sortWith(_.compare(_) < 0)
        val headFiltered: (List[Char], Int) = filterDifference(chars, chars.head)
        (chars.head, headFiltered._2) :: times(headFiltered._1)
    }
  }

  //Helper method takes TARGET character, recursively iterates through
  def filterDifference(chars: List[Char], target: Char): (List[Char], Int) = {
    chars match {
      case Nil => (Nil, 0)
      case _ =>
        val prev = filterDifference(chars.tail, target)
        if(chars.head == target) (prev._1, prev._2 + 1)
        else(chars.head :: prev._1, prev._2)
    }
  }

  /**
   * Returns a list of `Leaf` nodes for a given frequency table `freqs`.
   *
   * The returned list should be ordered by ascending weights (i.e. the
   * head of the list should have the smallest weight), where the weight
   * of a leaf is the frequency of the character.
   */
  //Method returns list of (Char, Int) tuples from least repeated to most
  def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = {
    freqs match {
      case List() => List()
      case (ch, i) :: t =>
        //t is a (ch, i) tuple, with sorting helper method applied
        insertBySort[Leaf](Leaf(ch, i), makeOrderedLeafList(t),
          (leaf1: Leaf, leaf2: Leaf) => leaf1.weight <= leaf2.weight)
    }
  }
//-----------------------------------------------------------------------------------------------
  //Insertion Sort Helper Method
  def insertBySort[T](newElem: T, list: List[T], comeFirst: (T, T) => Boolean): List[T] = {
    list match {
      case List() => List(newElem)
      //Join head and tail
      case tHead :: tTail =>
        if(comeFirst(newElem, tHead)) newElem :: list
        else tHead :: insertBySort(newElem, tTail, comeFirst)
    }
  }

  /*
    REMINDER ABOUT CURRYING
      - Point of currying is to have a function take in a single parameter, when it
        normally takes in multiple parameters
      - For cleaner code

      Ex: Curry this add function (=)
        add(a: Int, b: Int) = a + b

      Ex: CURRIED add function, option #1 (=>)
        add2(a: Int)(b: Int) => a + b

      Ex: CURRIED add function, option #2 (=>)
        add3(a: Int) = (b: Int) => a + b
  */

  //-----------------------------------------------------------------------------------------------
  /** Checks whether the list `trees` contains only one single code tree */
  def singleton(trees: List[CodeTree]): Boolean = !trees.isEmpty && trees.size == 1
    //trees.size == 1 is sufficient

  /**
   * The parameter `trees` of this function is a list of code trees ordered
   * by ascending weights.
   *
   * This function takes the first two elements of the list `trees` and combines
   * them into a single `Fork` node. This node is then added back into the
   * remaining elements of `trees` at a position such that the ordering by weights
   * is PRESERVED.
   *
   * If `trees` is a list of less than two elements, that list should be returned
   * unchanged.
   */
  def combine(trees: List[CodeTree]): List[CodeTree] = {
    def comeFirst(ct1: CodeTree, ct2: CodeTree): Boolean = {
      val ct1Weight = ct1 match {
        //Discern weights based on LEAF or FORK node
        case l: Leaf => l.weight
        case f: Fork => f.weight
      }

      ct2 match {
        //Investigate <= character
        case l: Leaf => ct1Weight <= l.weight
        case f: Fork => ct1Weight <= f.weight
      }
    }

    //Return CodeTree if it becomes truly empty
    if(trees.isEmpty || singleton(trees)) trees
    else {
      val newFork = makeCodeTree(trees.head, trees.tail.head)
      val left = trees.tail.tail
      left match {
        case List() => List(newFork)
        case head :: tail => insertBySort[CodeTree](newFork, left, comeFirst)
      }
    }
  }

  //-----------------------------------------------------------------------------------------------
  /**
   * This function will be called in the following way:
   *
   *   until(singleton, combine)(trees)
   *
   * where `trees` is of type `List[CodeTree]`, `singleton` and `combine` refer to
   * the two functions defined above.
   *
   * In such an invocation, `until` should call the two functions until the list of
   * code trees contains only one single tree, and then return that singleton list.
   */
  def until(done: List[CodeTree] => Boolean, merge: List[CodeTree] => List[CodeTree])(trees: List[CodeTree]): List[CodeTree] = {
    if(done(trees)) trees
    else until(done, merge)(merge(trees))
  }

  /**
   * This function creates a code tree which is optimal to encode the text `chars`.
   *
   * The parameter `chars` is an arbitrary text. This function extracts the character
   * frequencies from that text and creates a code tree based on them.
   */
  def createCodeTree(chars: List[Char]): CodeTree = {
    //REMEMBER: CodeTree is a tree with the (Char, Int) tuple as node archetype
    val charCountPairs = times(chars)
    val orderedLeaves = makeOrderedLeafList(charCountPairs)
    until(singleton, combine)(orderedLeaves)(0)
  }

  //==================================================================================================
  // Part 3: Decoding

  type Bit = Int
  def CONST_LEFT = 0
  def CONST_RIGHT = 1

  /**
   * This function decodes the bit sequence `bits` using the code tree `tree` and returns
   * the resulting list of characters.
   */
  def decode(tree: CodeTree, bits: List[Bit]): List[Char] = {
    //Traverse CodeTree
    def decodeRecursive(currentTree: CodeTree, bits: List[Bit]): List[Char] = {
        currentTree match {

        //Pattern Matching (if-else), where still traversing tree)
        case fork: Fork =>
          //Go down from ROOT node
          if(bits.isEmpty) Nil
          //Go LEFT from CodeTree
          else if(bits.head == CONST_LEFT) decodeRecursive(fork.left, bits.tail)
          //Go RIGHT from CodeTree
          else decodeRecursive(fork.right, bits.tail)

        //Pattern Matching (if-else), where traversing tree and hit leaf node
        case leaf: Leaf =>
          //Start from ROOT, if there are more bits
          leaf.char :: (if(bits.isEmpty) Nil else decodeRecursive(tree, bits))
      }
    }
    //Call decodeRecursive helper method to execute upon call for decode
    decodeRecursive(tree,bits)
  }

  /**
   * A Huffman coding tree for the French language.
   * Generated from the data given at
   *   http://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
   */
  val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s',121895),Fork(Leaf('d',56269),Fork(Fork(Fork(Leaf('x',5928),Leaf('j',8351),List('x','j'),14279),Leaf('f',16351),List('x','j','f'),30630),Fork(Fork(Fork(Fork(Leaf('z',2093),Fork(Leaf('k',745),Leaf('w',1747),List('k','w'),2492),List('z','k','w'),4585),Leaf('y',4725),List('z','k','w','y'),9310),Leaf('h',11298),List('z','k','w','y','h'),20608),Leaf('q',20889),List('z','k','w','y','h','q'),41497),List('x','j','f','z','k','w','y','h','q'),72127),List('d','x','j','f','z','k','w','y','h','q'),128396),List('s','d','x','j','f','z','k','w','y','h','q'),250291),Fork(Fork(Leaf('o',82762),Leaf('l',83668),List('o','l'),166430),Fork(Fork(Leaf('m',45521),Leaf('p',46335),List('m','p'),91856),Leaf('u',96785),List('m','p','u'),188641),List('o','l','m','p','u'),355071),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u'),605362),Fork(Fork(Fork(Leaf('r',100500),Fork(Leaf('c',50003),Fork(Leaf('v',24975),Fork(Leaf('g',13288),Leaf('b',13822),List('g','b'),27110),List('v','g','b'),52085),List('c','v','g','b'),102088),List('r','c','v','g','b'),202588),Fork(Leaf('n',108812),Leaf('t',111103),List('n','t'),219915),List('r','c','v','g','b','n','t'),422503),Fork(Leaf('e',225947),Fork(Leaf('i',115465),Leaf('a',117110),List('i','a'),232575),List('e','i','a'),458522),List('r','c','v','g','b','n','t','e','i','a'),881025),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u','r','c','v','g','b','n','t','e','i','a'),1486387)

  /**
   * What does the secret message say? Can you decode it?
   * For the decoding use the `frenchCode' Huffman tree defined above.
   */
  val secret: List[Bit] = List(0,0,1,1,1,0,1,0,1,1,1,0,0,1,1,0,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,1,0,1,0,1,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1)

  /** Write a function that returns the decoded secret */
  def decodedSecret: List[Char] = {
    val revealSecret = decode(frenchCode, secret)
    revealSecret
  }

  //==================================================================================================
  // Part 4a: Encoding using Huffman tree

  /**
   * This function encodes `text` using the code tree `tree`
   * into a sequence of bits.
   */

  //Opposite method of decode
    //I wonder if there is an automatic 'reversal' of a method..
  def encode(tree: CodeTree)(text: List[Char]): List[Bit] = {
    if(text.isEmpty) Nil
    else {
      val headEncoded: List[Bit] = encodeEach(tree, text.head, List())
      headEncoded ::: encode(tree)(text.tail)
        // ::: prepends an entire list
    }
  }

    //(!!!) Why is the encodeEach method NOT curried within encode??
    def encodeEach(tree: CodeTree, ch: Char, acc: List[Bit]): List[Bit] = {
      tree match {
        case fork: Fork =>
          if(fork.chars.contains(ch)) {
            val leftTraverse = encodeEach(fork.left, ch, acc ::: List(CONST_LEFT))
            if (!leftTraverse.isEmpty) leftTraverse
            else encodeEach(fork.right, ch, acc ::: List(CONST_RIGHT))
          }

          else Nil

        //When encoding has to stop because you reached the leaf nodes
        case leaf: Leaf => if (leaf.char == ch) acc else Nil
      }
    }

  //==================================================================================================
  // Part 4b: Encoding using code table

  type CodeTable = List[(Char, List[Bit])]
  //CodeTable data type is a list of characters with CORRESPONDING bit sequence

  /**
   * This function returns the bit sequence that represents the character `char` in
   * the code table `table`.
   */

  //Isolate sequence of bits for a particular character
  def codeBits(table: CodeTable)(char: Char): List[Bit] = {
    val head = table.head
    if(head._1 == char) head._2
    else codeBits(table.tail)(char)
  }

  /**
   * Given a code tree, create a code table which contains, for every character in the
   * code tree, the sequence of bits representing that character.
   *
   * Hint: think of a recursive solution: every sub-tree of the code tree `tree` is itself
   * a valid code tree that can be represented as a code table. Using the code tables of the
   * sub-trees, think of how to build the code table for the entire tree.
   */
  def convert(tree: CodeTree): CodeTable = {
    def prependLeftDirection(chAndBits: (Char, List[Bit])): (Char, List[Bit]) = {
      (chAndBits._1, CONST_LEFT :: chAndBits._2)
    }

    def prependRightDirection(chAndBits: (Char, List[Bit])): (Char, List[Bit]) = {
      (chAndBits._1, CONST_RIGHT :: chAndBits._2)
    }

    tree match {
      case leaf: Leaf => List((leaf.char, Nil))
      case fork: Fork =>
        val left = convert(fork.left) mapConserve prependLeftDirection
        val right = convert(fork.right) mapConserve prependRightDirection
        mergeCodeTables(left, right)
    }
  }

  /**
   * This function takes two code tables and merges them into one. Depending on how you
   * use it in the `convert` method above, this merge method might also do some transformations
   * on the two parameter code tables.
   */
  def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable = a ::: b
    //REMEMBER: Using ::: operator prepends an ENTIRE LIST

  /**
   * This function encodes `text` according to the code tree `tree`.
   *
   * To speed up the encoding process, it first converts the code tree to a code table
   * and then uses it to perform the actual encoding.
   */
  def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = {
    //Convert code tree to code table
    //Perform actual encoding based on code table
    val codeTable = convert(tree)

    def quickEncodeEach(text: List[Char]): List[Bit] = {
      text match {
        case List() => List()
        case head :: tail => codeBits(codeTable)(head) ::: quickEncodeEach(tail)
      }
    }

    quickEncodeEach(text)
  }
}

object Huffman extends Huffman
