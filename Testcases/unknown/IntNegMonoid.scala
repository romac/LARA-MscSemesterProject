
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object IntNegMonoid {

  abstract class Monoid[A] {

    def empty: A

    def append(x: A, y: A): A

    @law
    def law_leftIdentity = forall { (x: A) =>
      append(empty, x) == x
    }

    @law
    def law_rightIdentity = forall { (x: A) =>
      append(x, empty) == x
    }

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      append(append(x, y), z) == append(x, append(y, z))
    }

  }

  implicit object intNegMonoid extends Monoid[BigInt] {
    def empty: BigInt = 0
    def append(x: BigInt, y: BigInt): BigInt = x - y
  }

}

