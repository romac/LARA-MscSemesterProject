
import stainless.lang._
import stainless.annotation._

object NatTest {

  sealed abstract class Nat {

    final
    def +(m: Nat): Nat = (this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }) // ensuring (_ == m + this)

    final
    def *(m: Nat): Nat = (this match {
      case Zero()  => Zero()
      case Succ(n) => m + n * m
    }) // ensuring (_ == m * this)

  }

  final case class Succ(n: Nat) extends Nat
  final case class Zero()       extends Nat

  val One = Succ(Zero())

  @induct
  def addZero(n: Nat): Boolean = {
    Zero() + n == n &&
    n + Zero() == n
  } holds

  @induct
  def mulOne(n: Nat): Boolean = {
    One * n == n &&
    n * One == n
  } holds

  @induct
  def mulZero(n: Nat): Boolean = {
    Zero() * n == Zero() &&
    n * Zero() == Zero()
  } holds

}

