
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  abstract class Monoid[A] {

    def empty: A

    def append(x: A, y: A): A

    @law
    def law_leftIdentity(x: A): Boolean = {
      append(empty, x) == x
    }

    @law
    def law_rightIdentity(x: A): Boolean = {
      append(x, empty) == x
    }

    @law
    def law_associative(x: A, y: A, z: A): Boolean = {
      append(append(x, y), z) == append(x, append(y, z))
    }

  }

  implicit def intAddMonoidDef: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 0
    def append(x: Int, y: Int): Int = x + y
  }

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.append(x, fold(xs))
  }

  def test[A](implicit M: Monoid[A]) = {
    M.append(M.empty, M.empty) == M.empty
  } holds

}

