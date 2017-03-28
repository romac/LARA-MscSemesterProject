
import stainless.lang._

object Alice {

  def domain[A](r: Set[(A, A)]): Set[A] = {
    Set(r.theSet.values.map(_._1))
  } ensuring { forall { (x: A) => 
    ???
  } }

  def isTotal[A](s: Set[A], r: Set[(A, A)]): Boolean = {
    domain(r) == s
  }

  def conjecture[A](s: Set[A], r: Set[(A, A)]): Boolean = {
    require(s.size != 0 && isTotal(s, r))

    
  }.holds

}

