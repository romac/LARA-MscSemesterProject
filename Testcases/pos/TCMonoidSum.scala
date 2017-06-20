
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object TCMonoidSum {

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

  final case class Sum(value: BigInt)

  implicit def sumMonoid = new Monoid[Sum] {
    def empty: Sum = Sum(0)
    def append(x: Sum, y: Sum): Sum = Sum(x.value + y.value)
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

  def lemma_fold = {
    val xs: List[BigInt] = List(1, 2, 3)
    val sum = fold(xs.map(Sum(_)))
    sum.value == 6
  } holds

  def lemma_foldMap = {
    val xs: List[BigInt] = List(1, 2, 3)
    val sum = foldMap(xs)(Sum(_))
    sum.value == 6
  } holds

}

