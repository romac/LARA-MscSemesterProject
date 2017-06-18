
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object MonoidTC {

  case class Monoid[A](empty: A, op: (A, A) => A) {

    require {
      law_leftIdentity  &&
      law_rightIdentity &&
      law_associative
    }

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

  // sealed abstract class List[A] {

  //   final def ++(other: List[A]): List[A] = (this match {
  //     case Nil()       => other
  //     case Cons(x, xs) => Cons(x, xs ++ other)
  //   }) ensuring { res =>
  //     (res.content == this.content ++ that.content) &&
  //     (res.size == this.size + that.size) &&
  //     (that != Nil[T]() || res == this)
  //   }

  // }

  // final case class Cons[A](head: A, tail: List[A]) extends List[A]
  // final case class Nil[A]() extends List[A]

  // def listMonoid[A]: Monoid[List[A]] =
  //   Monoid(Nil(), _ ++ _)

  val intAddMonoid: Monoid[Int] = Monoid[Int](0, _ + _)
  // => VALID

  val intAddMonoid_broken: Monoid[Int] = Monoid[Int](0, _ - _)
  // - Now considering 'body assertion' VC for intAddMonoid_broken @?:?...
  // => INVALID
  // Found counter-example:
  //  (Empty model)

  def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
    case Nil()       => M.empty
    case Cons(x, xs) => M.op(x, fold(xs))
  }

  def fold_lemma1 = {
    fold(Nil())(intAddMonoid) == 0
  } holds // => VALID

  def fold_lemma2 = {
    fold(42 :: Nil())(intAddMonoid) == 42
  } holds // => VALID

  def fold_lemma3 = {
    fold(1 :: 2 :: Nil())(intAddMonoid) == 3
  } holds // => VALID


  def fold_lemma3_broken = {
    fold(1 :: 2 :: Nil())(intAddMonoid_broken) == 3
  } holds // => VALID (ideally shouldn't, but we can see this as assuming that the laws hold for any given Monoid)

  def fold_lemma4 = {
    fold(1 :: 2 :: 3 :: Nil())(intAddMonoid) == 6
  } holds // ... HANGS ...

  // val intMulMonoid: Monoid[Int] = Monoid[Int](1, _ * _)
  // // ... HANGS ...

  // def mkOptionMonoid[A](m: Monoid[A]): Monoid[Option[A]] = {
  //   Monoid(
  //     None(),
  //     (x: Option[A], y: Option[A]) => (x, y) match {
  //       case (Some(x0), Some(y0)) =>
  //         Some(m.op(x0, y0))

  //       case (None(), y0) =>
  //         y0

  //       case (x0, None()) =>
  //         x0

  //       case _ =>
  //         None()
  //     }
  //   )
  // }

  // // ... HANGS ...

  // def mkOptionMonoid_broken[A](m: Monoid[A]): Monoid[Option[A]] = {
  //   Monoid(
  //     None(),
  //     (x: Option[A], y: Option[A]) => (x, y) match {
  //       case (Some(x0), Some(y0)) =>
  //         Some(m.op(x0, y0))

  //       case _ =>
  //         x
  //     }
  //   )
  // }

  // // - Now considering 'body assertion' VC for mkOptionMonoid_broken @?:?...
  // // => INVALID
  // // Found counter-example:
  // //  m: Monoid[A] -> Monoid[A](A#4, (x$887: A, x$888: A) => if (x$887 == A#4 && x$888 == A#4) {
  // //    A#4
  // //  } else if (x$887 == A#4) {
  // //    A#4
  // //  } else if (x$888 == A#4) {
  // //    A#4
  // //  } else if (true) {
  // //    A#4
  // //  } else {
  // //    A#4
  // //  })

}

