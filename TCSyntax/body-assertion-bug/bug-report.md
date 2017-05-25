@nv: This doesn't really look like a bug to me, but rather, as we discussed the other day, something we could improve when making use of `@induct`ive lemmas in assertions.

The following testcase

```scala
import stainless.lang._
import stainless.proof._
import stainless.annotation._

object Monoid_Nat_proof {

  sealed abstract class Nat {
    def +(m: Nat): Nat = this match {
      case Zero()  => m
      case Succ(n) => Succ(n + m)
    }
  }
  case class Succ(n: Nat) extends Nat
  case class Zero()       extends Nat

  @induct
  def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
    Zero() + n == n
  } holds

  @induct
  def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
    n + Zero() == n
  } holds

  case class PseudoMonoid[A](empty: A, combine: (A, A) => A) {
    require {
      forall { (x: A) => combine(empty, x) == x } &&
      forall { (x: A) => combine(x, empty) == x }
    }
  }

  def natPlusPseudoMonoid: PseudoMonoid[Nat] = {
    assert {
      forall { (x: Nat) => lemma_leftIdentityZeroPlus(x) } &&
      forall { (x: Nat) => lemma_rightIdentityZeroPlus(x) }
    }

    PseudoMonoid[Nat](Zero(), _ + _)
  }
}
```

timeouts when verifying 'adt invariant'

```
[  Info  ]  - Now considering 'adt invariant' VC for natPlusPseudoMonoid @?:?...
[ Debug  ] instExpr: ∀x: Nat. lemma_leftIdentityZeroPlus(x) && ∀x: Nat. lemma_rightIdentityZeroPlus(x) && {
[ Debug  ]   val thiss: PseudoMonoid[Nat] = PseudoMonoid[Nat](Zero(), fun2[Nat, Nat, Nat]((x$1: Nat, x$2: Nat) => +(x$1, x$2), (x$1: Nat, x$2: Nat) => true))
[ Debug  ]   ¬∀x$244: Nat, x$245: Nat. thiss.combine.pre(x$244, x$245) || ¬∀x: Nat. (thiss.combine.f(thiss.empty, x) == x) || ¬∀x: Nat. (thiss.combine.f(x, thiss.empty) == x)
[ Debug  ] }
[Warning ]  => TIMEOUT
```

(full log here: https://gist.github.com/e625e5bb3bef21bb3a25e616481df1e5)

Adding an @inline annotation to lemmas

```diff
diff --git a/no-inline.scala b/inline.scala
index bc9143f..7c13816 100644
--- a/no-inline.scala
+++ b/inline.scala
@@ -13,11 +13,13 @@ object Monoid_Nat_proof {
   case class Succ(n: Nat) extends Nat
   case class Zero()       extends Nat
 
+  @inline
   @induct
   def lemma_leftIdentityZeroPlus(n: Nat): Boolean = {
     Zero() + n == n
   } holds
 
+  @inline
   @induct
   def lemma_rightIdentityZeroPlus(n: Nat): Boolean = {
     n + Zero() == n
```

makes the 'adt invariant' VC verify but it's now 'body assertion' which timeouts

```
[  Info  ]  - Now considering 'body assertion' VC for natPlusPseudoMonoid @?:?...
[ Debug  ] instExpr: +(Zero(), x) ≠ x
[  Info  ]  => VALID
[  Info  ]  - Now considering 'body assertion' VC for natPlusPseudoMonoid @?:?...
[ Debug  ] instExpr: ∀x: Nat. {
[ Debug  ]   val res: Boolean = +(Zero(), x) == x
[ Debug  ]   assume(res)
[ Debug  ]   res
[ Debug  ] } && +(x, Zero()) ≠ x
[Warning ]  => TIMEOUT
```

(full log here: https://gist.github.com/b6dce3cf1804d3d5c632ff2caa3d15f5)

Applying the following patch to the code listed at the top makes everything verify again:

```diff
diff --git a/no-inline.scala b/manual-inline.scala
index bc9143f..f0ab159 100644
--- a/no-inline.scala
+++ b/manual-inline.scala
@@ -32,8 +32,8 @@ object Monoid_Nat_proof {
 
   def natPlusPseudoMonoid: PseudoMonoid[Nat] = {
     assert {
-      forall { (x: Nat) => lemma_leftIdentityZeroPlus(x) } &&
-      forall { (x: Nat) => lemma_rightIdentityZeroPlus(x) }
+      forall { (x: Nat) => (Zero() + x == x) because lemma_leftIdentityZeroPlus(x) } &&
+      forall { (x: Nat) => (x + Zero() == x) because lemma_rightIdentityZeroPlus(x) }
     }
 
     PseudoMonoid[Nat](Zero(), _ + _)
```

(full log here: https://gist.github.com/0334d391a78b450683ef22895a2fc3de)

