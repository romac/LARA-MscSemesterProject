
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object TCBigIntMonoid {

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

  implicit object bigIntMonoid extends Monoid[BigInt] {
    def empty: BigInt = 0
    def append(x: BigInt, y: BigInt): BigInt = x + y
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

}

