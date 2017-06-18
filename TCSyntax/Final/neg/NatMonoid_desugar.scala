
import stainless.lang._
import stainless.annotation._
import stainless.proof._

object Monoid_Nat_desugar {

  case class Monoid[A](empty: A, combine: (A, A) => A) {

    require(law_leftIdentity && law_rightIdentity && law_associative)

    def law_leftIdentity = forall { (x: A) =>
      combine(empty, x) == x
    }

    def law_rightIdentity = forall { (x: A) =>
      combine(x, empty) == x
    }

    def law_associative = forall { (x: A, y: A, z: A) =>
      combine(combine(x, y), z) == combine(x, combine(y, z))
    }

  }

  sealed abstract class Nat {

    def +(m: Nat): Nat = this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }

  }

  final case class Succ(n: Nat) extends Nat
  final case class Zero()       extends Nat

  @inline
  @induct
  def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
    Zero() + n == n
  } holds

  @inline
  @induct
  def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
    n + Zero() == n
  } holds

  @inline
  @induct
  def lemma_associativePlus(n: Nat, m: Nat, l: Nat): Boolean = {
    (n + m) + l == n + (m + l)
  } holds

  def natPlusMonoid: Monoid[Nat] = {
    require {
      forall((x: Nat)                 => lemma_leftIdentityZeroPlus(x)) &&
      forall((x: Nat)                 => lemma_rightIdentityZeroPlus(x)) &&
      forall((x: Nat, y: Nat, z: Nat) => lemma_associativePlus(x, y, z))
    }

    Monoid[Nat](Zero(), _ + _)
  }

}

