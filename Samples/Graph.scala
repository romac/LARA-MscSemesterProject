
import stainless.lang._
import stainless.annotation._

object graph {

  case class Graph[G, V](
    empty   : G,
    vertex  : V => G,
    overlay : (G, G) => G,
    connect : (G, G) => G
  ) {

    def law_empty = {
      forall((x: G) => overlay(empty, x) == x) &&
      forall((x: G) => connect(empty, x) == x)
    }

    def law_decomp = forall((x: G, y: G, z: G) => {
      connect(connect(x, y), z) == overlay(overlay(connect(x, y), connect(x, z)), connect(y, z))
    })

  }

}

