
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
      addZero && mulOne && mulZero
    }

    private
    def addZero = forall { (x: A) =>
      add(zero, x) == x &&
      add(x, zero) == x
    }

    private
    def mulOne = forall { (x: A) =>
      mul(one, x) == x &&
      mul(x, one) == x
    }

    private
    def mulZero = forall { (x: A) =>
      mul(zero, x) == zero &&
      mul(x, zero) == zero
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
      one  = 1,
      add  = _ + _,
      mul  = _ * _
    )

  // => VALID

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

