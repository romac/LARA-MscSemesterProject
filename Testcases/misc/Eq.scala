

import stainless.lang._
import stainless.annotation._

object EqTC {

  case class Eq[A](eq: (A, A) => Boolean) {

    require {
      forall((x: A)             => eq(x, x)) &&
      forall((x: A, y: A)       => eq(x, y) == eq(y, x)) &&
      forall((x: A, y: A, z: A) => (eq(x, y) && eq(y, z)) ==> eq(x, z))
    }

    def neq(x: A, y: A): Boolean =
      !eq(x, y)

  }

  val intEq: Eq[Int] =
    Eq(_ == _)

  // => VALID

  val intEq_broken: Eq[Int] =
    Eq(
      (x: Int, y: Int) =>
        x - y == 10
    )

  //  - Now considering 'body assertion' VC for intEq_broken @?:?...
  //  => INVALID
  // Found counter-example:
  //   (Empty model)

  case class Ord[A](E: Eq[A], lte: (A, A) => Boolean) {

    require {
      forall((x: A, y: A, z: A) => (lte(x, y) && lte(y, z)) ==> lte(x, z))  &&
      forall((x: A, y: A)       => (lte(x, y) && lte(y, x)) ==> E.eq(x, y)) &&
      forall((x: A, y: A)       => lte(x, y) || lte(y, x))
    }

    def lt(x: A, y: A): Boolean =
      E.neq(x, y) || lte(x, y)

    def gt(x: A, y: A): Boolean =
      !lte(x, y)

    def gte(x: A, y: A): Boolean =
      !lt(x, y)

  }

  val intOrd: Ord[Int] =
    Ord(intEq, _ <= _)

  // => VALID

  val intOrd_broken1: Ord[Int] =
    Ord(intEq, (x: Int, b: Int) => false)

  // ... HANGS...

  val intOrd_broken2: Ord[Int] =
    Ord(intEq_broken, _ <= _)

  // ... HANGS ...

  /*
   * The following breaks Stainless with
   *
   *   Eq.scala:57:5: Could not extract new EqTC.Ord[Int] (Scala tree of type class scala.reflect.internal.Trees$New)
   *
   *
   * case class Ord[A](lte: (A, A) => Boolean)(E: Eq[A]) {
   *
   *   require {
   *     forall((x: A, y: A, z: A) => (lte(x, y) && lte(y, z)) ==> lte(x, z))  &&
   *     forall((x: A, y: A)       => (lte(x, y) && lte(y, x)) ==> E.eq(x, y)) &&
   *     forall((x: A, y: A)       => lte(x, y) || lte(y, x))
   *   }
   *
   *   def lt(x: A, y: A): Boolean =
   *     E.neq(x, y) || lte(x, y)
   *
   *   def gt(x: A, y: A): Boolean =
   *     !lte(x, y)
   *
   *   def gte(x: A, y: A): Boolean =
   *     !lt(x, y)
   *
   * }
   *
   * val intOrd: Ord[Int] =
   *   Ord((x: Int, y: Int) => x <= y)(intEq)
   *
   * // => VALID
   *
   * val intOrd_broken1: Ord[Int] =
   *   Ord((x: Int, b: Int) => false)(intEq)
   *
   * val intOrd_broken2: Ord[Int] =
   *   Ord((x: Int, y: Int) => x <= y)(intEq_broken)
   */

}

