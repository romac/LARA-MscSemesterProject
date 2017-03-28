
import stainless.lang.forall
import stainless.annotation._
// import stainless.collection._

object MonoidTC {

  case class Monoid[A](empty: A, op: (A, A) => A) {

    require {
      law_leftIdentity  &&
      law_rightIdentity &&
      law_associative
    }

    def twice: (A, A) = (empty, empty)

    def law_leftIdentity = forall { (x: A) =>
      op(empty, x) == x
    }

    def law_rightIdentity = forall { (x: A) =>
      op(x, empty) == x
    }

    def law_associative = forall { (x: A, y: A, z: A) =>
      op(op(x, y), z) == op(x, op(y, z))
    }
  }

  val intAddMonoid: Monoid[Int] = Monoid[Int](0, _ + _)

  // def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
  //   case Nil()       => M.empty
  //   case Cons(x, xs) => M.op(x, fold(xs))
  // }

}

