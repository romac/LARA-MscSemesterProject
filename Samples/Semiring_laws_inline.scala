
import stainless.lang._
import stainless.annotation._

object SemiringTC {

  case class Semiring[A](
    zero : A,
    one  : A,
    add  : (A, A) => A,
    mul  : (A, A) => A
  ) {

    require {
      forall((x: A, y: A, z: A) => add(add(x, y), z) == add(x, add(y, z)))         &&
      forall((x: A)             => add(zero, x) == x)                              &&
      forall((x: A)             => add(x, zero) == x)                              &&
      forall((x: A, y: A)       => add(x, y) == add(y, x))                         &&
      forall((x: A, y: A, z: A) => mul(mul(x, y), z) == mul(x, mul(y, z)))         &&
      forall((x: A)             => mul(one, x) == x)                               &&
      forall((x: A)             => mul(x, one) == x)                               &&
      forall((x: A, y: A, z: A) => mul(x, add(y, z)) == add(mul(x, y), mul(x, z))) &&
      forall((x: A, y: A, z: A) => mul(add(x, y), z) == add(mul(x, z), mul(y, z))) &&
      forall((x: A)             => mul(zero, x) == zero)                           &&
      forall((x: A)             => mul(x, zero) == zero)
    }

  }

  sealed abstract class Nat {

    final
    def +(m: Nat): Nat = this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }

    final
    def *(m: Nat): Nat = this match {
      case Zero()  => Zero()
      case Succ(n) => m + n * m
    }

  }

  final case class Succ(n: Nat) extends Nat
  final case class Zero()       extends Nat

  val natSemiring: Semiring[Nat] =
    Semiring(
      zero = Zero(),
      one  = Succ(Zero()),
      add  = _ + _,
      mul  = _ * _
    )

  // ... HANGS ...

  val intAddSemiring: Semiring[Int] =
    Semiring(
      zero = 0,
      one = 1,
      add = _ + _,
      mul = _ * _
    )

  // ... HANGS ...

  val boolConjSemiring: Semiring[Boolean] =
    Semiring(
      zero = false,
      one  = true,
      add  = _ || _,
      mul  = _ && _
    )

  // => VALID

  val boolDisjSemiring: Semiring[Boolean] =
    Semiring(
      zero = true,
      one  = false,
      add  = _ && _,
      mul  = _ || _
    )

  // => VALID

}

