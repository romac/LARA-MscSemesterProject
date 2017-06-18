
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object uniplate {

  sealed abstract class Expr
  final case class Add(left: Expr, right: Expr) extends Expr
  final case class Sub(left: Expr, right: Expr) extends Expr
  final case class Mul(left: Expr, right: Expr) extends Expr
  final case class Div(left: Expr, right: Expr) extends Expr
  final case class Neg(expr: Expr)              extends Expr
  final case class Val(value: Int)              extends Expr
  final case class Var(name: String)            extends Expr

  @coherent
  abstract class Uniplate[A] {

    def uniplate(a: A): (List[A], List[A] => A)

    @law
    def law_identity = forall { (x: A) =>
      val (childs, ctx) = uniplate(x)
      ctx(childs) == x
    }

  }

  object Uniplate {

    def children[A](a: A)(implicit U: Uniplate[A]): List[A] =
      U.uniplate(a)._1

    def universe[A](a: A)(implicit U: Uniplate[A]): List[A] =
      a :: children(a).flatMap(universe(_))

    def transform[A](f: A => A, a: A)(implicit U: Uniplate[A]): A = {
      val (childs, ctx) = U.uniplate(a)
      f(ctx(childs.map(x => transform(f, x))))
    }

    def descend[A](f: A => A, a: A)(implicit U: Uniplate[A]): A = {
      val (childs, ctx) = U.uniplate(a)
      ctx(childs.map(f))
    }

    def rewrite[A](f: A => Option[A], a: A)(implicit U: Uniplate[A]): A = {
      def g(x: A): A = f(x) match {
        case None()   => x
        case Some(x0) => rewrite(f, x0)
      }

      transform(g, a)
    }

  }

  implicit def exprUniplate = new Uniplate[Expr] {

    override
    def uniplate(expr: Expr): (List[Expr], List[Expr] => Expr) = expr match {
      case Neg(a) => (
        List(a),
        (as: List[Expr]) => {
          require(as.size == 1)
          Neg(as.head)
        }
      )

      case Add(a, b) => (
        List(a, b),
        (as: List[Expr]) => {
          require(as.size == 2)
          Add(as.head, as.tail.head)
        }
      )

      case Sub(a, b) => (
        List(a, b),
        (as: List[Expr]) => {
          require(as.size == 2)
          Sub(as.head, as.tail.head)
        }
      )

      case Mul(a, b) => (
        List(a, b),
        (as: List[Expr]) => {
          require(as.size == 2)
          Mul(as.head, as.tail.head)
        }
      )

      case Div(a, b) => (
        List(a, b),
        (as: List[Expr]) => {
          require(as.size == 2)
          Div(as.head, as.tail.head)
        }
      )

      case x => (
        Nil(),
        (as: List[Expr]) => {
          require(as.size == 0)
          x
        }
      )

    }
  }

  def nf(expr: Expr): Option[Expr] = expr match {
    case Sub(x, y)           => Some(Add(x, Neg(y)))
    case Add(x, y) if x == y => Some(Mul(x, Val(2)))
    case _                   => None()
  }

  def rewriteNF(expr: Expr): Expr = {
    Uniplate.rewrite(nf, expr)(exprUniplate)
  }

  val x = Sub(Neg(Var("q")), Var("q"))

  def test = {
    rewriteNF(x) == Mul(Var("q"), Val(2))
  } holds

}

