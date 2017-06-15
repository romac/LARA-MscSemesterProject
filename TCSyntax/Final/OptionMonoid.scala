
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object OptionMonoid {

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

  object Monoid {
    def apply[A](implicit M: Monoid[A]): Monoid[A] = M
  }

  implicit def optionMonoid[A](implicit M: Monoid[A]): Monoid[Option[A]] = new Monoid[Option[A]] {
    def empty: Option[A] = None()

    def append(x: Option[A], y: Option[A]): Option[A] = (x, y) match {
      case (None(), a) => a
      case (a, None()) => a
      case (Some(a), Some(b)) => Some(M.append(a, b))
    }
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

}

