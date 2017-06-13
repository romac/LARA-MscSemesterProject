
import stainless.lang._
import stainless.annotation._
import stainless.collection._

object TCEq {

  @coherent
  abstract class Monoid[A] {
    def empty: A
    def append(x: A, y: A): A
  }

  def foo: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 0
    def append(x: Int, y: Int): Int = x + y
  }

  def lemma_eq[A](a: Monoid[A], b: Monoid[A]) = {
    a == b
  } holds

  val set1 = Set(foo)
  val set2 = Set(foo, foo)

  def lemma_set = {
    set1 == set2
  } holds

}

