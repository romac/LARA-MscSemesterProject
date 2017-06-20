
import stainless.lang._
import stainless.annotation._

object TCEqOrdPartial {

  @coherent
  abstract class Eq[A] {

    def eq(x: A, y: A): Boolean

    def neq(x: A, y: A): Boolean =
      !eq(x, y)

    @law
    def law_reflexive: Boolean = forall {(x: A) =>
      eq(x, x)
    }

    @law
    def law_symetric: Boolean = forall { (x: A, y: A) =>
      eq(x, y) == eq(y, x)
    }

    // @law
    // def law_transitive: Boolean = forall { (x: A, y: A, z: A) =>
    //   (eq(x, y) && eq(y, z)) ==> eq(x, z)
    // }

  }

  implicit val intEq: Eq[BigInt] = new Eq[BigInt] {
    def eq(x: BigInt, y: BigInt) = x == y
  }

  @coherent
  abstract class Ord[A](implicit val eq: Eq[A]) {

    def lte(x: A, y: A): Boolean

    // @law
    // def law_transitive: Boolean = forall { (x: A, y: A, z: A) =>
    //   (lte(x, y) && lte(y, z)) ==> lte(x, z)
    // }

    // @law
    // def law_eq: Boolean = forall { (x: A, y: A) =>
    //   (lte(x, y) && lte(y, x)) ==> eq.eq(x, y)
    // }

    @law
    def law_antisymetric: Boolean = forall { (x: A, y: A) =>
      lte(x, y) || lte(y, x)
    }

    def lt(x: A, y: A): Boolean =
      eq.neq(x, y) || lte(x, y)

    def gt(x: A, y: A): Boolean =
      !lte(x, y)

    def gte(x: A, y: A): Boolean =
      !lt(x, y)

  }

  implicit val bigIntOrd: Ord[BigInt] = new Ord[BigInt] {
    def lte(x: BigInt, y: BigInt) = x <= y
  }

}

