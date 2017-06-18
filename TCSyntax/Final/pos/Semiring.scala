
import stainless.lang._
import stainless.annotation._

object SemiringTC {

  abstract class Semiring[A] {
    def zero: A
    def one: A
    def add(x: A, y: A): A
    def mul(x: A, y: A): A

    @law def law1  = forall((x: A, y: A, z: A) => add(add(x, y), z) == add(x, add(y, z)))
    @law def law2  = forall((x: A)             => add(zero, x) == x)
    @law def law3  = forall((x: A)             => add(x, zero) == x)
    @law def law4  = forall((x: A, y: A)       => add(x, y) == add(y, x))
    @law def law5  = forall((x: A, y: A, z: A) => mul(mul(x, y), z) == mul(x, mul(y, z)))
    @law def law6  = forall((x: A)             => mul(one, x) == x)
    @law def law7  = forall((x: A)             => mul(x, one) == x)
    @law def law8  = forall((x: A, y: A, z: A) => mul(x, add(y, z)) == add(mul(x, y), mul(x, z)))
    @law def law9  = forall((x: A, y: A, z: A) => mul(add(x, y), z) == add(mul(x, z), mul(y, z)))
    @law def law10 = forall((x: A)             => mul(zero, x) == zero)
    @law def law11 = forall((x: A)             => mul(x, zero) == zero)
  }

  // val intAddSemiring: Semiring[Int] =
  //   Semiring(
  //     zero = 0,
  //     one = 1,
  //     add = _ + _,
  //     mul = _ * _
  //   )

  // ... HANGS ...

  val boolConjSemiring: Semiring[Boolean] = new Semiring[Boolean] {
    def zero = false
    def one  = true
    def add(x: Boolean, y: Boolean) = x || x
    def mul(x: Boolean, y: Boolean)  = x && y
  }

  // => VALID

  val boolDisjSemiring: Semiring[Boolean] = new Semiring[Boolean] {
    def zero = true
    def one  = false
    def add(x: Boolean, y: Boolean) = x && x
    def mul(x: Boolean, y: Boolean)  = x || y
  }

  // => VALID

}

