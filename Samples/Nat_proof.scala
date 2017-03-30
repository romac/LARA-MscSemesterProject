
import stainless.lang._
import stainless.collection._
import stainless.annotation._
import stainless.proof._

object NatTest {

  sealed abstract class Nat {

    def +(m: Nat): Nat = this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }

  }

  final case class Succ(n: Nat) extends Nat
  final case class Zero()       extends Nat

  def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
    Zero() + n == n
  } holds

  def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
    (n + Zero() == n) because {
      n match {
        case Zero()  => trivial
        case Succ(m) => lemma_rightIdentityZeroPlus(m)
      }
    }
  } holds

  def lemma_associativePlus(n: Nat, m: Nat, l: Nat): Boolean = {
    (n + (m + l) == (n + m) + l) because {
      n match {
        case Zero() =>
          trivial

        case Succ(n1) => {
          Succ(n1).asInstanceOf[Nat] + (m + l) ==| trivial                         |
          Succ(n1 + (m + l)).asInstanceOf[Nat] ==| lemma_associativePlus(n1, m, l) |
          Succ((n1 + m) + l).asInstanceOf[Nat] ==| trivial                         |
          (Succ(n1) + m) + l
        }.qed
      }
    }
  } holds

}

