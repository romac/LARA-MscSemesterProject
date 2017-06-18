
import stainless.lang._
import stainless.annotation._
import stainless.collection._
import stainless.proof._

object ListMonoid {

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
    @inline
    def apply[A](implicit M: Monoid[A]): Monoid[A] = M
  }

  // implicit class MonoidSyntax[A](self: A)(implicit val M: Monoid[A]) {
  //   def <>(other: A): A = M.append(self, other)
  // }

  @induct
  @inline
  def lemma_listLeftIdentity[A](x: List[A]) = {
    Nil() ++ x == x
  }.holds

  @induct
  @inline
  def lemma_listRightIdentity[A](x: List[A]) = {
    x ++ Nil() == x
  }.holds

  @induct
  @inline
  def lemma_listAssoc[A](x: List[A], y: List[A], z: List[A]) = {
    (x ++ y) ++ z == x ++ (y ++ z)
  }.holds

  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {

    override def empty: List[A] =
      Nil()

    override def append(x: List[A], y: List[A]): List[A] =
      x ++ y

    override def law_leftIdentity = forall { (x: List[A]) =>
      super.law_leftIdentity because lemma_listLeftIdentity(x)
    }

    override def law_rightIdentity = forall { (x: List[A]) =>
      super.law_rightIdentity because lemma_listRightIdentity(x)
    }

    override def law_associative = forall { (x: List[A], y: List[A], z: List[A]) =>
      super.law_associative because lemma_listAssoc(x, y, z)
    }
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
    fold(list.map(f))
  }

  def test_append = {
    val xs: List[List[BigInt]] = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))
    val res: List[BigInt] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    fold(xs) == res
  }.holds

}

