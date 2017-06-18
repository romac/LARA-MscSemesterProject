
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object FirstMonoid {

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

  final case class First[A](getFirst: Option[A])

  object First {
    def apply[A](a: A): First[A] = First(Some(a))
  }

  implicit def firstMonoid[A]: Monoid[First[A]] = new Monoid[First[A]] {
    def empty: First[A] = First(None[A]())
    def append(x: First[A], y: First[A]): First[A] = x.getFirst match {
      case Some(a) => x
      case None() => y
    }
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

  // def lemma_foldMap_first = {
  //   val xs = List(true, false, false)
  //   val first = foldMap(xs)(First(_)).getFirst
  //   first == Some(true)
  // } holds

}

