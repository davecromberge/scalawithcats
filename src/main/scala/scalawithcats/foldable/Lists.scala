package scalawithcats.foldable

object Lists {
  // Foldable abstracts over foldLeft and foldRight.  This allows
  // us to write generic folds once and for all that work with a
  // variety of sequence types.

  // - FoldLeft combines the elements of a sequence from start to finish.
  // - FoldRight combines the elements of a sequence from finish to start.
  // These two operations are equivalent, so long as the operation is
  // associative.

  import cats.Foldable
  import cats.instances.list._
  
  val ints = List(1, 2, 3)
  Foldable[List].foldLeft(ints, 0)(_ + _)

  // Foldable defines foldRight differently to the foldLeft function, 
  // in terms of the Eval monad.
  // 
  // def foldRight[A, B](fa: F[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B]
  // 
  // This provides stack safety, for example when folding right over a stream:

  import cats.Eval
  val data = (1 to 100000).toList
  Foldable[List].foldRight(data, cats.Eval.now(0L))((i, e) => e.map(_ + 1))

  // Cats provides additional common functions such as exists, find etc
  // that are defined in terms of foldLeft.  The foldMap function takes a 
  // supplied function, maps it over the elements and combines the results
  // using a monoid for the accumulator B.

  // Foldables may be composed for deep traversal:
  import cats.instances.vector._
  (Foldable[List] compose Foldable[Vector]) // will combine a list of vectors

  // Syntax is available for foldable, that allows us to call foldMap or
  // combineAll directly on the receiver.  Scala will use explictly defined
  // functions in place of implicits where the function to fold is already
  // defined on the receiver.
}