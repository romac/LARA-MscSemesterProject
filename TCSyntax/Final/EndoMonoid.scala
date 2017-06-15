
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object EndoMonoid {

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

  final case class Endo[A](appEndo: A => A)

  implicit def endoMonoid[A]: Monoid[Endo[A]] = new Monoid[Endo[A]] {

    def empty: Endo[A] =
      Endo(x => x)

    def append(f: Endo[A], g: Endo[A]): Endo[A] =
      Endo(x => f.appEndo(g.appEndo(x)))

  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

  def lemma_foldMap_endo = {
    val fs: List[Int => Int] = List(_ + 2, _ * 10)
    val composed = foldMap(fs)(Endo(_))
    composed.appEndo(2) == 42
  } holds

}

