
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  case class Semigroup[A](combine: (A, A) => A) {
    require {
      forall { (x: A, y: A, z: A) =>
        combine(combine(x, y), z) == combine(x, combine(y, z))
      }
    }
  }

  implicit def intAddSemigroup: Semigroup[Int] = Semigroup[Int](_ + _)

  case class Monoid[A](semigroup: Semigroup[A], empty: A) {
    require {
      forall { (x: A) => semigroup.combine(empty, x) == x && semigroup.combine(x, empty) == x }
    }
  }

  implicit def intAddMonoidDef: Monoid[Int] = Monoid[Int](intAddSemigroup, 0)

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.semigroup.combine(x, fold(xs))
  }

  def test[A](implicit M: Monoid[A]) = {
    M.semigroup.combine(M.empty, M.empty) == M.empty
  } holds

}

