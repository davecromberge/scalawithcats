package scalawithcats.semigroupal

object Option {
  // Functors and monads sequence computations with map and flatMap.
  // When computations are independent, a semigroup can compose
  // pairs of contexts together.  The difference between semigroupal
  // and semigroup is that semigroupal combines contexts, while semigroup
  // combines values. 

  // If we have an F[A] and a F[B], and a Semigroupal[F], we can
  // use the semigroupal to create an F[(A, B)]:
  // Semigroupal[F].product(fa, fb)

  import cats.Semigroupal
  import cats.instances.option._
  import cats.syntax.option._

  val cat = Semigroupal[Option].product(Some(1), Some("cat")) // Some((1, "cat"))

  // The companion object for Semigroupal defines tupleN, contramapN, mapN, imapN etc:
  val cat2 = Semigroupal.tuple3(1.some, "cat".some, 3.some)
  // There is a shorthand apply syntax for the tupleN variants:
  import cats.syntax.apply._

  (1.some, "cat".some).tupled
  (1.some, "cat".some).mapN((i, name) => s"$i cat(s) called $name")

  // In cats, monads extend Semigroupal and define product in terms of map and
  // flatMap.  This does not give us the behaviour we expect, as composing values
  // in these contexts should be independent.  However, the semantics of monads
  // are preserved by treating them in the same way to normal monad composition.
  // - Using Semigroupal[Future] (starts executing futures before product)
  // - Semigroupal[Either] (stops evaluating at first failure)
  // - Semigroupal[List] (cross product of lists instead of tuple)

  // Implement product in terms of flatMap:
  import cats.Monad
  import cats.syntax.flatMap._
  import cats.syntax.functor._

  def product[M[_]: Monad, A, B](ma: M[A], mb: M[B]): M[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)
}