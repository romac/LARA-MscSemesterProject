
import stainless.lang._
import stainless.annotation._

object Monoid_Nat_proof {

  // @typeclass
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



  def natPlusMonoid: Monoid[Nat] = {
    assert(forall { (x: Int) => lemma_leftIdentityZeroPlus(x) })

    new Monoid[Nat] {

    def empty: Nat              = Zero()

    def combine(a: Nat, b: Nat) = a + b


    override def law_leftIdentity(x: Int) =
      super.law_leftIdentity because lemma_leftIdentityZeroPlus(X)

    // @lawProof(law_leftIdentity)
    // def proof_leftIdentity(x: Nat) =
    //   lemma_leftIdentityZeroPlus(x)

    @lawProof(law_rightIdentity)
    def proof_leftIdentity(x: Nat) =
      lemma_rightIdentityZeroPlus(x)

    @lawProof(law_associative)
    def proof_associative(x: Nat, y: Nat, z: Nat) =
      lemma_associativePlus(x, y, z)
  }

}

