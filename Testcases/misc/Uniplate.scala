
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

  sealed abstract class Childs {
    def size: Int = this match {
      case Zero()    => 0
      case One(_)    => 1
      case Two(_, _) => 2
    }

    def toList: List[Expr] = this match {
      case Zero()    => Nil()
      case One(a)    => a :: Nil()
      case Two(a, b) => a :: b :: Nil()
    }

    def map(f: Expr => Expr): Childs = this match {
      case Zero()    => Zero()
      case One(a)    => One(f(a))
      case Two(a, b) => Two(f(a), f(b))
    }
  }

  final case class Zero()                extends Childs
  final case class One(a: Expr)          extends Childs
  final case class Two(a: Expr, b: Expr) extends Childs

  def children(a: Expr): List[Expr] =
    uniplate(a)._1.toList

  def universe(a: Expr): List[Expr] =
    a :: children(a).flatMap(universe(_))

  def transform(f: Expr => Expr, a: Expr): Expr = {
    val (childs, ctx) = uniplate(a)
    f(ctx(childs.map(x => transform(f, x))))
  }

  def descend(f: Expr => Expr, a: Expr): Expr = {
    val (childs, ctx) = uniplate(a)
    ctx(childs.map(f))
  }

  def rewrite(f: Expr => Option[Expr], a: Expr): Expr = {
    def g(x: Expr): Expr = f(x) match {
      case None()   => x
      case Some(x0) => rewrite(f, x0)
    }

    transform(g, a)
  }

  def nf(expr: Expr): Option[Expr] = expr match {
    case Sub(x, y)           => Some(Add(x, Neg(y)))
    case Add(x, y) if x == y => Some(Mul(x, Val(2)))
    case _                   => None()
  }

  def rewriteNF(expr: Expr): Expr =
    rewrite(nf, expr)

  val x = Sub(Neg(Var("q")), Var("q"))

  def test = {
    rewriteNF(x) == Mul(Neg(Var("q")), Val(2))
  } holds

  def uniplate(expr: Expr): (Childs, Childs => Expr) =
    expr match {

      case Neg(a) => (
        One(a),
        (as: Childs) => {
          require(as.size == 1)
          val One(a0) = as
          Neg(a0)
        }
      )

      case Add(a, b) => (
        Two(a, b),
        (as: Childs) => {
          require(as.size == 2)
          val Two(a0, b0) = as
          Add(a0, b0)
        }
      )

      case Sub(a, b) => (
        Two(a, b),
        (as: Childs) => {
          require(as.size == 2)
          val Two(a0, b0) = as
          Sub(a0, b0)
        }
      )

      case Mul(a, b) => (
        Two(a, b),
        (as: Childs) => {
          require(as.size == 2)
          val Two(a0, b0) = as
          Mul(a0, b0)
        }
      )

      // case Div(a, b) => (
      //   List(a, b),
      //   (as: Childs) => {
      //     require(as.size == 2)
      //     Div(as.head, as.tail.head)
      //   }
      // )

      case x => (
        Zero(),
        (as: Childs) => {
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

