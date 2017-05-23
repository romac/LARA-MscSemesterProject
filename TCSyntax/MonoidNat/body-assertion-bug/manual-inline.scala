import stainless.lang._
import stainless.proof._
import stainless.annotation._

object Monoid_Nat_proof {

  sealed abstract class Nat {
    def +(m: Nat): Nat = this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }
  }
  case class Succ(n: Nat) extends Nat
  case class Zero()       extends Nat

  @induct
  def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
    Zero() + n == n
  } holds

  @induct
  def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
    n + Zero() == n
  } holds

  case class PseudoMonoid[A](empty: A, combine: (A, A) => A) {
    require {
      forall { (x: A) => combine(empty, x) == x } &&
      forall { (x: A) => combine(x, empty) == x }
    }
  }

  def natPlusPseudoMonoid: PseudoMonoid[Nat] = {
    assert {
      forall { (x: Nat) => (Zero() + x == x) because lemma_leftIdentityZeroPlus(x) } &&
      forall { (x: Nat) => (x + Zero() == x) because lemma_rightIdentityZeroPlus(x) }
    }

    PseudoMonoid[Nat](Zero(), _ + _)
  }

}

