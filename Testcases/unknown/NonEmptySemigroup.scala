
import stainless.lang._
import stainless.annotation._
import stainless.collection._
import stainless.proof._

object NonEmptySemigroup {

  @coherent
  abstract class Semigroup[A] {

    def combine(x: A, y: A): A

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      combine(combine(x, y), z) == combine(x, combine(y, z))
    }

  }

  object Semigroup {
    def apply[A](implicit S: Semigroup[A]): Semigroup[A] = S
  }

  final case class NonEmpty[A](list: List[A]) {

    require(list.nonEmpty)

    def ++(that: NonEmpty[A]): NonEmpty[A] = {
      NonEmpty(this.list ++ that.list)
    }

    def <>(that: NonEmpty[A])(implicit S: Semigroup[NonEmpty[A]]): NonEmpty[A] =
      this ++ that

  }

  @induct
  def lemma_list_assoc[A](xs: List[A], ys: List[A], zs: List[A]) = {
    (xs ++ ys) ++ zs == xs ++ (ys ++ zs)
  } ensuring { res => res }

  def lemma_ne_list_assoc[A](xs: NonEmpty[A], ys: NonEmpty[A], zs: NonEmpty[A]) = {
    ((xs ++ ys) ++ zs == xs ++ (ys ++ zs)) because lemma_list_assoc(xs.list, ys.list, zs.list)
  } ensuring { res => res }

  implicit def nonEmptySemigroup[A]: Semigroup[NonEmpty[A]] = new Semigroup[NonEmpty[A]] {
    override def combine(x: NonEmpty[A], y: NonEmpty[A]): NonEmpty[A] = x ++ y

    override def law_associative = forall { (x: NonEmpty[A], y: NonEmpty[A], z: NonEmpty[A]) =>
      super.law_associative because lemma_ne_list_assoc(x, y, z).holds
    }
  }

  def lemma_combine = {
    val x = NonEmpty(List(1, 2, 3))
    val y = NonEmpty(List(4, 5, 6))

    x <> y == NonEmpty(List(1, 2, 3, 4, 5, 6))
  } holds

  def lemma_combine_ne = {
    val x = NonEmpty(List(1, 2, 3))
    val y = NonEmpty(List(4, 5, 6))

    (x <> y).list.nonEmpty
  } holds

}

