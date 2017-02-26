
import stainless.lang._
import stainless.annotation._

object SemigroupTC {

  case class Semigroup[A](append: (A, A) => A) {

    require {
      forall((x: A, y: A, z: A) => append(append(x, y), z) == append(x, append(y, z)))
    }

  }

  val intAddSemigroup: Semigroup[Int] = Semigroup[Int](_ + _)
  // => VALID

  val intAddSemigroup_broken: Semigroup[Int] = Semigroup[Int](_ - _)
  // - Now considering 'body assertion' VC for intAddSemigroup_broken @?:?...
  // => INVALID
  // Found counter-example:
  //    (Empty model)

  val intMulSemigroup: Semigroup[Int] = Semigroup[Int](_ * _)
  // - Now considering 'body assertion' VC for intMulSemigroup @?:?...
  // ... HANGS ...

  @induct
  def mkOptionSemigroup[A](m: Semigroup[A]): Semigroup[Option[A]] = {
    Semigroup(
      (x: Option[A], y: Option[A]) => (x, y) match {
        case (Some(x0), Some(y0)) =>
          Some(m.append(x0, y0))

        case (None(), y0) =>
          y0

        case (x0, None()) =>
          x0

        case _ =>
          None()
      }
    )
  }

  def mkOptionSemigroup_broken[A](m: Semigroup[A]): Semigroup[Option[A]] = {
    Semigroup(
      (x: Option[A], y: Option[A]) => (x, y) match {
        case (Some(x0), Some(y0)) =>
          Some(m.append(x0, y0))

        case _ =>
          x
      }
    )
  }

}

