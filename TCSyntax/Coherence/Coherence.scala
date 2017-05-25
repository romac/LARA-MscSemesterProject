
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

  def bar: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 0
    def append(x: Int, y: Int): Int = x + y
  }

  def baz: Monoid[Int] = new Monoid[Int] {
    def empty: Int = 1
    def append(x: Int, y: Int): Int = x * y
  }

  def lemma_foo_bar = {
    foo == bar
  } holds

  def lemma_foo_baz = {
    foo == baz
  } holds

  def lemma_bar_baz = {
    bar == baz
  } holds

  def lemma_eq[A](a: Monoid[A], b: Monoid[A]) = {
    a == b
  } holds

}

