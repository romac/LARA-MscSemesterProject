
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  abstract class Semigroup[A] {
    def combine(x: A, y: A): A

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      combine(combine(x, y), z) == combine(x, combine(y, z))
    }
  }

  implicit def intAddSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def combine(x: Int, y: Int): Int = x + y
  }

  abstract class Monoid[A](implicit val semigroup: Semigroup[A]) {
  // abstract class Monoid[A] {

  //   implicit def semigroup: Semigroup[A]

    def empty: A

    // @inline
    // def combine(x: A, y: A): A = semigroup.combine(x, y)

    @law
    def law_leftIdentity = forall { (x: A) =>
      semigroup.combine(empty, x) == x
    }

    @law
    def law_rightIdentity = forall { (x: A) =>
      semigroup.combine(x, empty) == x
    }

  }

  implicit def intAddMonoidDef: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 0
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.semigroup.combine(x, fold(xs))
  }

  def test[A](implicit M: Monoid[A]) = {
    M.semigroup.combine(M.empty, M.empty) == M.empty
  } holds

}

