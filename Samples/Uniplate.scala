
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object uniplate {

  sealed abstract class Expr
  final case class Add(left: Expr, right: Expr) extends Expr
  final case class Sub(left: Expr, right: Expr) extends Expr
  final case class Mul(left: Expr, right: Expr) extends Expr
  // final case class Div(left: Expr, right: Expr) extends Expr
  final case class Neg(expr: Expr)              extends Expr
  final case class Val(value: Int)              extends Expr
  final case class Var(name: String)            extends Expr

  // def children(a: Expr): List[Expr] =
  //   uniplate(a)._1

  // def universe(a: Expr): List[Expr] =
  //   a :: children(a).flatMap(universe(_))

  // def transform(f: Expr => Expr, a: Expr): Expr = {
  //   val (childs, ctx) = uniplate(a)
  //   f(ctx(childs.map(x => transform(f, x))))
  // }

  // def descend(f: Expr => Expr, a: Expr): Expr = {
  //   val (childs, ctx) = uniplate(a)
  //   ctx(childs.map(f))
  // }

  // def rewrite(f: Expr => Option[Expr], a: Expr): Expr = {
  //   def g(x: Expr): Expr = f(x) match {
  //     case None()   => x
  //     case Some(x0) => rewrite(f, x0)
  //   }

  //   transform(g, a)
  // }

  // def nf(expr: Expr): Option[Expr] = expr match {
  //   case Sub(x, y)           => Some(Add(x, Neg(y)))
  //   case Add(x, y) if x == y => Some(Mul(x, Val(2)))
  //   case _                   => None()
  // }

  // def rewriteNF(expr: Expr): Expr =
  //   rewrite(nf, expr)

  // val x = Sub(Neg(Var("q")), Var("q"))

  // def test = {
  //   rewriteNF(x) == Mul(Var("q"), Val(2))
  // } holds

  def uniplate(expr: Expr): (List[Expr], List[Expr] => Expr) =
    expr match {

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

      // case Div(a, b) => (
      //   List(a, b),
      //   (as: List[Expr]) => {
      //     require(as.size == 2)
      //     Div(as.head, as.tail.head)
      //   }
      // )

      case x => (
        Nil(),
        (as: List[Expr]) => {
          require(as.size == 0)
          x
        }
      )

    }

  def uniplate_law(expr: Expr): Boolean = {
    val (childs, ctx) = uniplate(expr)
    ctx(childs) == expr
  } holds

}

