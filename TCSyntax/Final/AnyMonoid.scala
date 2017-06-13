
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object SumMonoid {

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

  final case class Any(value: Boolean)

  implicit def anyMonoid = new Monoid[Any] {
    def empty: Any = Any(false)
    def append(x: Any, y: Any): Any = Any(x.value || y.value)
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

  def lemma_foldMap_false = {
    val xs = List(false, false, false)
    val sum = foldMap(xs)(Any(_))
    sum.value == false
  } holds

  def lemma_foldMap_true = {
    val xs = List(false, true, false)
    val sum = foldMap(xs)(Any(_))
    sum.value == true
  } holds

}

