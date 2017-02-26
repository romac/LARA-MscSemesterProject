
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
      forall((x: A)             => add(zero, x) == add(x, zero))                   &&
      forall((x: A)             => add(zero, x) == x)                              &&
      forall((x: A)             => add(x, zero) == x)                              &&
      forall((x: A, y: A)       => add(x, y) == add(y, x))                         &&
      forall((x: A, y: A, z: A) => mul(mul(x, y), z) == mul(x, mul(y, z)))         &&
      forall((x: A)             => mul(one, x) == mul(x, one) == x)                &&
      forall((x: A, y: A, z: A) => mul(x, add(y, z)) == add(mul(x, y), mul(x, z))) &&
      forall((x: A, y: A, z: A) => mul(add(x, y), z) == add(mul(x, z), mul(y, z))) &&
      forall((x: A)             => mul(zero, x) == mul(x, zero))                   &&
      forall((x: A)             => mul(zero, x) == zero)                           &&
      forall((x: A)             => mul(x, zero) == zero)
    }

  }

  val intAddSemiring: Semiring[Int] =
    Semiring[Int](
      zero = 0,
      one = 1,
      add = _ + _,
      mul = _ * _
    )

    // - Now considering 'precond. (call inv[Int](Semiring[Int](0, 1, (x$1: Int,  ...)' VC for intAddSemiring @?:?...
    // => VALID
    // - Now considering 'body assertion' VC for intAddSemiring @?:?...
    // Error: Z3 exception

}

