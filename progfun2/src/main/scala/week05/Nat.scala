package week05

abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat
  def + (that: Nat): Nat
  def - (that: Nat): Nat
}

object Zero extends Nat {
  def isZero = true
  def predecessor: Nat = throw new Error("0.predecessor")
  def successor = new Succ(this)
  def +(that: Nat)
  def -(that: Nat)
}

//Succ helper method
class Succ(n: Nat) extends Nat {
  def isZero = false
  def predecessor = n
  def successor = new Succ(this)
}

