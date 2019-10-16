package scalawithcats. monad

object Eval {
  // Eval abstracts over two models of execution,
  // both eager and lazy.
  //
  // this is memoized and eager, and is similar to a val
  val now = cats.Eval.now(1) 
  // this is memoized and lazy, and is similar to a lazy val
  val later = cats.Eval.later {  
    println("Hi from the future")
  }
  // this is non-memoized and lazy, and is similar to a def
  val always = cats.Eval.always {
    println("Hello again")
  }

  // since Eval is a monad, map and flatMap over eval
  // creates a chain of functions that are lazily
  // evaluated until the .value is called.
  cats.Eval.later(3 * 3).flatMap(a => cats.Eval.later(3 * a)).value

  // intermediate chains can be memoized to prevent
  // re-evaluation and introduce caching:
  cats.Eval.later(3 * 3).memoize.flatMap(a => cats.Eval.later(3 * a)).value

  // Eval makes of a trampoline technique to store
  // function calls on the heap, not the stack.
  // This allows us to avoid a stackoverflow error in:
  def factorial(n: Int): cats.Eval[Int] =
    if (n == 1)
      cats.Eval.now(1)
    else
      cats.Eval.defer(factorial(n - 1).map(_ * n))

  // stack-safe implementation of foldRight
  def foldRightEval[A, B](as: List[A], acc: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] =
    as match {
      case head :: tail => 
        cats.Eval.defer(f(head, foldRightEval(tail, acc)(f)))
      case Nil =>
        acc
    }

  def foldRight[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
    foldRightEval(as, cats.Eval.now(acc)) { case (a, b) =>
      b.map(f(a, _))
    }.value
}

