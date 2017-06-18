
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object FunctorTC {

  abstract class Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  class OptionFunctorClass extends Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
      case None() => None()
      case Some(a) => Some(f(a))
    }
  }

  // def optionFunctorMethod: Functor[Option] = new OptionFunctorClass

  // object OptionFunctorObject extends Functor[Option] {
  //   override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
  //     case None() => None()
  //     case Some(a) => Some(f(a))
  //   }
  // }

  // implicit def optionFunctor: Functor[Option] = new Functor[Option] {
  //   override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa match {
  //     case None() => None()
  //     case Some(a) => Some(f(a))
  //   }
  // }

}

