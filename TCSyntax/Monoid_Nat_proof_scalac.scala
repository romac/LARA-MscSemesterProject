
import scala.annotation.Annotation

object Monoid_Nat_proof {

  class law    extends Annotation
  class induct extends Annotation

  implicit class BooleanDecorations(val underlying: Boolean) {

    @inline
    def holds: Boolean = {
      underlying
    } ensuring {
      (res: Boolean) => res
    }

    @inline
    def holds(becauseOfThat: Boolean): Boolean = {
      underlying
    } ensuring {
      (res: Boolean) => becauseOfThat && res
    }

    @inline
    def ==>(that: => Boolean): Boolean = {
      if (underlying) that else true
    }

    def because(proof: Boolean): Boolean = proof && underlying

  }

  abstract class Monoid[A] {

    def empty: A

    def combine(x: A, y: A): A

    @law
    def law_leftIdentity(x: A): Boolean = {
      combine(empty, x) == x
    }

    @law
    def law_rightIdentity(x: A): Boolean = {
      combine(x, empty) == x
    }

    @law
    def law_associative(x: A, y: A, z: A): Boolean = {
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

  @induct
  def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
    Zero() + n == n
  } holds

  @induct
  def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
    n + Zero() == n
  } holds

  @induct
  def lemma_associativePlus(n: Nat, m: Nat, l: Nat): Boolean = {
    (n + m) + l == n + (m + l)
  } holds

  def natPlusMonoid: Monoid[Nat] = new Monoid[Nat] {

    override def empty: Nat = Zero()

    override def combine(a: Nat, b: Nat) = a + b

    override def law_leftIdentity(x: Nat) =
      super.law_leftIdentity(x) because lemma_leftIdentityZeroPlus(x)

    override def law_rightIdentity(x: Nat) =
      super.law_rightIdentity(x) because lemma_rightIdentityZeroPlus(x)

    override def law_associative(x: Nat, y: Nat, z: Nat) =
      super.law_associative(x, y, z) because lemma_associativePlus(x, y, z)
  }

  // def natPlusMonoid: Monoid[Nat] = {
  //   assert {
  //     forall { (x: Nat)                 => lemma_leftIdentityZeroPlus(x)  } &&
  //     forall { (x: Nat)                 => lemma_rightIdentityZeroPlus(x) } &&
  //     forall { (x: Nat, y: Nat, z: Nat) => lemma_associativePlus(x, y, z) }
  //   }

  //   Monoid[Nat](() => Zero(), (a: Nat, b: Nat) => a + b)
  // }

}

