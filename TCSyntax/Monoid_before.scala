
import stainless.lang.forall
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  case class Semigroup[A](semigroup_op: (A, A) => A)

  @typeclass
  abstract class Monoid[A] {
  // abstract class Monoid[A](semigroup: Semigroup[A]) {

    def monoid_empty: A

    def monoid_op(x: A, y: A): A

    def monoid_twice: (A, A) = (monoid_empty, monoid_empty)

    // TODO
    // @law
    // def law_leftIdentity(x: A): Boolean = {
    //   monoid_op(monoid_empty, x) == x
    // }

    @law
    def law_leftIdentity = forall { (x: A) =>
      monoid_op(monoid_empty, x) == x
    }

    @law
    def law_rightIdentity = forall { (x: A) =>
      monoid_op(x, monoid_empty) == x
    }

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      monoid_op(monoid_op(x, y), z) == monoid_op(x, monoid_op(y, z))
    }

  }

  // case class Monoid[A](semigroup: Semigroup[A], empty: A, op: (A, A) => A) {

  //   require {
  //     forall { (x: A) => law_leftIdentity(x) } &&
  //     law_rightIdentity &&
  //     law_associative
  //   }

  //   def twice: (A, A) = (empty, empty)

  //   def law_leftIdentity = forall { (x: A) =>
  //     op(empty, x) == x
  //   }

  //   def law_rightIdentity = forall { (x: A) =>
  //     op(x, empty) == x
  //   }

  //   def law_associative = forall { (x: A, y: A, z: A) =>
  //     op(op(x, y), z) == op(x, op(y, z))
  //   }
  // }

  @instance
  object intAddMonoidObj extends Monoid[Int] {
    def monoid_empty: Int         = 0
    def monoid_op(a: Int, b: Int) = a + b
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  @instance
  def intAddMonoidDef = new Monoid[Int] {
    def monoid_empty: Int         = 0
    def monoid_op(a: Int, b: Int) = a + b
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  @instance
  val intAddMonoidVal = new Monoid[Int] {
    def monoid_empty: Int         = 0
    def monoid_op(a: Int, b: Int) = a + b
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.monoid_empty
    case Cons(x, xs) => M.monoid_op(x, fold(xs))
  }

}

