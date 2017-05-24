
sealed abstract class List[A] {

  def size: BigInt = this match {
    case Nil() => 0
    case Cons(_, xs) => 1 + xs.size
  }

}

final case class Cons[A](head: A, tail: List[A]) extends List[A]
final case class Nil[A]() extends List[A]

