
import stainless.lang._
import stainless.collection._
import stainless.annotation._
import stainless.proof._

object TCNewtype {

  @coherent
  abstract class Newtype[N, O] {
    def pack(o: O): N
    def unpack(n: N): O

    @law
    def law_identity1 = forall { (o: O) =>
      unpack(pack(o)) == o
    }

    @law
    def law_identity2 = forall { (n: N) =>
      pack(unpack(n)) == n
    }

  }

  object Newtype {

    def ala[N0, O0, N1, O1, B](pa: O0 => N0, hof: B => (O0 => N0) => N1)(b: B)
      (implicit NT0: Newtype[N0, O0], NT1: Newtype[N1, O1]): O1 = {
      NT1.unpack(hof(b)(NT0.pack _))
    }

  }

}

// object SumMonoid {

//   @coherent
//   abstract class Monoid[A] {

//     def empty: A

//     def append(x: A, y: A): A

//     @law
//     def law_leftIdentity = forall { (x: A) =>
//       append(empty, x) == x
//     }

//     @law
//     def law_rightIdentity = forall { (x: A) =>
//       append(x, empty) == x
//     }

//     @law
//     def law_associative = forall { (x: A, y: A, z: A) =>
//       append(append(x, y), z) == append(x, append(y, z))
//     }

//   }

//   final case class Sum(value: BigInt)

//   implicit def sumMonoid = new Monoid[Sum] {
//     def empty: Sum = Sum(0)
//     def append(x: Sum, y: Sum): Sum = Sum(x.value + y.value)
//   }

//   def fold[A](list: List[A])(implicit M: Monoid[A]): A = list match {
//     case Nil() => M.empty
//     case Cons(x, xs) => M.append(x, fold(xs))
//   }

//   def foldMap[A, B](list: List[A])(f: A => B)(implicit M: Monoid[B]): B = {
//     fold(list.map(f))
//   }

// }

object test {

  import NT._
  import SumMonoid._

  implicit def sumNewtype: Newtype[Sum, BigInt] = new Newtype[Sum, BigInt] {
    def pack(o: BigInt): Sum = Sum(o)
    def unpack(n: Sum): BigInt = n.value
  }

  def sum(list: List[BigInt]): BigInt = {
    Newtype.ala((x: BigInt) => Sum(x), foldMap[BigInt, Sum] _)(list)
  }

  def lemma = {
    val list: List[BigInt] = List(1, 2, 3, 4)
    val res: BigInt = sum(list)
    res == 10
  } holds

}

