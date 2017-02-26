
import stainless.lang._
import stainless.annotation._

object MonoidTC {

  case class Monoid[A](empty: A, append: (A, A) => A) {

    require {
      forall((x: A)             => append(empty, x) == x) &&
      forall((x: A)             => append(x, empty) == x) &&
      forall((x: A, y: A, z: A) => append(append(x, y), z) == append(x, append(y, z)))
    }

  }

  val intAddMonoid: Monoid[Int] = Monoid[Int](0, _ + _)
  // => VALID

  val intAddMonoid_broken: Monoid[Int] = Monoid[Int](0, _ - _)
  // - Now considering 'body assertion' VC for intAddMonoid_broken @?:?...
  // => INVALID
  // Found counter-example:
  //    (Empty model)

  val intMulMonoid: Monoid[Int] = Monoid[Int](1, _ * _)
  // - Now considering 'body assertion' VC for intMulMonoid @?:?...
  // ... HANGS ...

  def mkOptionMonoid[A](m: Monoid[A]): Monoid[Option[A]] = {
    Monoid(
      None(),
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

  // - Now considering 'body assertion' VC for mkOptionMonoid @?:?...
  // ... HANGS ...

  def mkOptionMonoid_broken[A](m: Monoid[A]): Monoid[Option[A]] = {
    Monoid(
      None(),
      (x: Option[A], y: Option[A]) => (x, y) match {
        case (Some(x0), Some(y0)) =>
          Some(m.append(x0, y0))

        case _ =>
          x
      }
    )
  }

  // - Now considering 'body assertion' VC for mkOptionMonoid @?:?...
  // => INVALID
  // Found counter-example:
  //  m: Monoid[A] -> Monoid[A](A#2, (x$742: A, x$743: A) => if (x$742 == A#2 && x$743 == A#2) {
  //    A#2
  //  } else if (x$743 == A#2) {
  //    A#2
  //  } else if (x$742 == A#2) {
  //    A#2
  //  } else if (true) {
  //    A#2
  //  } else {
  //    A#2
  //  })

}

