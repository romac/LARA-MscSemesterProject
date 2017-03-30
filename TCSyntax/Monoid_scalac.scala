
import scala.annotation.Annotation

object MonoidTC {

  class typeclass  extends Annotation
  class law        extends Annotation
  class instance   extends Annotation

  def forall[A](p: A => Boolean): Boolean = sys.error("Can't execute quantified proposition")
  def forall[A,B](p: (A,B) => Boolean): Boolean = sys.error("Can't execute quantified proposition")
  def forall[A,B,C](p: (A,B,C) => Boolean): Boolean = sys.error("Can't execute quantified proposition")

  @typeclass
  abstract class Semigroup[A] {
    def semigroup_op(x: A, y: A): A

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      semigroup_op(semigroup_op(x, y), z) == semigroup_op(x, semigroup_op(y, z))
    }
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

    @law
    def law_associative = forall { (x: A, y: A, z: A) =>
      monoid_op(monoid_op(x, y), z) == monoid_op(x, monoid_op(y, z))
    }

  }

  @instance
  implicit def intAddSemigroup = new Semigroup[Int] {
    def semigroup_op(x: Int, y: Int): Int = x + y
  }

  @instance
  object intAddMonoidObj extends Monoid[Int] {
    def monoid_empty: Int = 0
  }

  @instance
  def intAddMonoidDef = new Monoid[Int] {
    def monoid_empty: Int = 0
  }

  @instance
  val intAddMonoidVal = new Monoid[Int] {
    def monoid_empty: Int = 0
  }

}

