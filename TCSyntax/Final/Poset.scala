
import stainless.lang._
import stainless.annotation._
import stainless.proof._

object PosetTC {

  sealed abstract class PosetOrd
  final case class EQ() extends PosetOrd
  final case class LT() extends PosetOrd
  final case class GT() extends PosetOrd
  final case class NC() extends PosetOrd

  @coherent
  abstract class Poset[A] {
    def compare(x: A, y: A): PosetOrd

    @inline
    def leq(x: A, y: A): Boolean = compare(x, y) match {
      case EQ() | LT() => true
      case _           => false
    }

    @law
    def law_reflexive = forall { (x: A) =>
      leq(x, x) == true
    }

  }

  implicit object posetFloat extends Poset[Float] {
    def compare(x: Float, y: Float): PosetOrd = (x, y) match {
      case (Float.NaN, Float.NaN) => NC()
      case (a, b) if a == b       => EQ()
      case (a, b) if a < b        => LT()
      case (a, b)                 => GT()
    }
  }

}

