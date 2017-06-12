
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object FunctorTC {

  abstract class Functor[F[_]] {

    def map[A, B](fa: F[A])(f: A => B): F[B]

    @law
    def law_identity[A] = forall { (fa: F[A]) =>
      map(fa)(x => x) == fa
    }

    @law
    def law_distributive[A, B, C] = forall { (fa: F[A], f: B => C, g: A => B) =>
      map(fa)(x => f(g(x))) == map(map(fa)(g))(f)
    }

  }

}


