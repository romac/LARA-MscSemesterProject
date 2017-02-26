
import stainless.lang._
import stainless.annotation._

object MonoidTC {

  case class Monoid[A](empty: A, append: (A,A) => A) {

    require {
      law_leftIdentity &&
      law_rightIdentity &&
      law_associativity
    }

    @inline
    private
    def law_leftIdentity = forall { (x: A) =>
      append(empty, x) == x
    }

    @inline
    private
    def law_rightIdentity = forall { (x: A) =>
      append(x, empty) == x
    }

    @inline
    private
    def law_associativity = forall { (x: A, y: A, z: A) =>
      append(append(x, y), z) == append(x, append(y, z))
    }
  }

  val intAddMonoid: Monoid[Int] = Monoid[Int](0, _ + _)
  // => VALID

  val intAddMonoid_broken: Monoid[Int] = Monoid[Int](0, _ - _)
  // - Now considering 'body assertion' VC for intAddMonoid_broken @?:?...
  // => INVALID
  // Found counter-example:
  //  (Empty model)


  val intMulMonoid: Monoid[Int] = Monoid[Int](1, _ * _)
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

   // - Now considering 'body assertion' VC for mkOptionMonoid_broken @?:?...
   // => INVALID
   // Found counter-example:
   //  m: Monoid[A] -> Monoid[A](A#4, (x$887: A, x$888: A) => if (x$887 == A#4 && x$888 == A#4) {
   //    A#4
   //  } else if (x$887 == A#4) {
   //    A#4
   //  } else if (x$888 == A#4) {
   //    A#4
   //  } else if (true) {
   //    A#4
   //  } else {
   //    A#4
   //  })

}

