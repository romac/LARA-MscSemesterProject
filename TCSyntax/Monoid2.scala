
import stainless.lang.forall
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  @typeclass
  abstract class Semigroup[A] {
    def semigroup_op(x: A, y: A): A

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      semigroup_op(semigroup_op(x, y), z) == semigroup_op(x, semigroup_op(y, z))
    }
  }

  implicit def intAddSemigroup = new Semigroup[Int] {
    def semigroup_op(x: Int, y: Int): Int = x + y
  }

  @typeclass
  abstract class Monoid[A](implicit val semigroup: Semigroup[A]) {

    def monoid_empty: A

    def monoid_op(x: A, y: A): A = semigroup.semigroup_op(x, y)

    @law
    def law_leftIdentity = forall { (x: A) =>
      monoid_op(monoid_empty, x) == x
    }

    @law
    def law_rightIdentity = forall { (x: A) =>
      monoid_op(x, monoid_empty) == x
    }

  }

  // case class Monoid[A](semigroup: Semigroup[A], empty: A) {

  //   require {
  //     law_leftIdentity && law_rightIdentity
  //   }

  //   def op(x: A, y: A): A = semigroup.op(x, y)

  //   def law_leftIdentity = forall { (x: A) =>
  //     op(empty, x) == x
  //   }

  //   def law_rightIdentity = forall { (x: A) =>
  //     op(x, empty) == x
  //   }

  // }

  implicit object intAddMonoidObj extends Monoid[Int] {
    def monoid_empty: Int = 0
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  def intAddMonoidDef = new Monoid[Int] {
    def monoid_empty: Int = 0
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  val intAddMonoidVal = new Monoid[Int] {
    def monoid_empty: Int = 0
  }

  // def intAddMonoidObj = Monoid[Int](0, a + b)

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.monoid_empty
    case Cons(x, xs) => M.monoid_op(x, fold(xs))
  }

}

