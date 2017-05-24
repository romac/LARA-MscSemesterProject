
object foobar {

  case class Foo[A](toto: A => A)

  case class Bar[A](foo: Foo[A]) {
    def tata(a: A): A = foo.toto(a)
  }

}

