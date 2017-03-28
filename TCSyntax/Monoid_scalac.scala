
import scala.annotation.Annotation

object MonoidTC {

  class typeclass  extends Annotation
  class law        extends Annotation
  class instance   extends Annotation

  def forall[A](p: A => Boolean): Boolean = sys.error("Can't execute quantified proposition")
  def forall[A,B](p: (A,B) => Boolean): Boolean = sys.error("Can't execute quantified proposition")
  def forall[A,B,C](p: (A,B,C) => Boolean): Boolean = sys.error("Can't execute quantified proposition")

  @typeclass
  abstract class Monoid[A] {

    def monoid_empty: A

    def monoid_op(x: A, y: A): A

    def monoid_twice: (A, A) = (monoid_empty, monoid_empty)

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

  // case class Monoid[A](empty: A, op: (A, A) => A) {

  //   require {
  //     law_leftIdentity  &&
  //     law_rightIdentity &&
  //     law_associative
  //   }
  //
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

  @instance
  def intAddMonoidDef = new Monoid[Int] {
    def monoid_empty: Int         = 0
    def monoid_op(a: Int, b: Int) = a + b
  }

  @instance
  val intAddMonoidVal = new Monoid[Int] {
    def monoid_empty: Int         = 0
    def monoid_op(a: Int, b: Int) = a + b
  }

  // val intAddMonoid: Monoid[Int] = Monoid[Int](0, _ + _)

  // def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
  //   case Nil()       => M.empty
  //   case Cons(x, xs) => M.op(x, fold(xs))
  // }

}

