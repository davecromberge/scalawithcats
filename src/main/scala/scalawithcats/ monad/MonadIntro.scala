package scalawithcats.monad

import cats.Monad
import cats.Functor
import cats.Applicative
import cats.FlatMap

object MonadIntro {
  // Monads provide a mechanism to sequence computations.
  // Functors provide sequencing, but what to do after
  // each step is fixed, where monads allow the course
  // of action in the sequence to be dynamic as the output
  // of one step is fed to the next.
  // ---
  // Every monad is a functor, and has to obey the functor
  // laws (identity and composition) 
  // Monadic behaviour is captured by two fundamantal
  // operations:

  // abstracts over type constructors
  def pure[F[_], A](a: A): F[A] = ???

  def flatMap[F[_], A, B](fa: F[A])(f: A => F[B]): F[B] = ???
  // ---
  // In addition to the functor laws, monads should obey the
  // monad laws of left and right identity, and associativity.

  // map may be defined in terms of pure and flatMap:
  def map[F[_], A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => pure(f(a)))

  // The cats monad type class extends the Applicative typeclass
  // for pure, and the FlatMap typeclass for the flatMap
  // capabalities.  Applicative extends Functor, which provides
  // the Monad with the map capability.

  // Cats provides instances of most standard library types,
  // as well as an identity monad:
  type Id[A] = A

  import cats.instances.list._
  Monad[List].flatMap(List(1, 2, 3))(a => List(a * 10))

  import cats.instances.option._
  // Syntax imports
  import cats.syntax.applicative._ // for pure
  3.pure[Option]
  
  import cats.syntax.flatMap._     // for flatMap
  import cats.syntax.functor._     // for map
  def flatMapEx[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(a0 => b.map(b0 => a0 * b0))

  // typeclass instances for Id
  implicit val idFunctor: Functor[Id] = new Functor[Id] {
    override def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
  }

  // typeclass instance for applicative
  implicit val idApplicative: Applicative[Id] = new Applicative[Id] {
    override def pure[A](a: A): Id[A] = a
    override def ap[A, B](ff: Id[A => B])(fa: Id[A]): Id[B] = ff(fa)
  }

  // typeclass instance for flatMap
  implicit val idFlatMap: FlatMap[Id] = new FlatMap[Id] {
    override def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
    override def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa)
    override def tailRecM[A, B](fa: Id[A])(f: A => Either[A, B]): Id[B] = ???
  }

  // Cats provides a right-biased Either map and flatMap that
  // can be imported via the either instances.
  // Either syntax also supports syntax:
  import cats.syntax.either._
  val either: Either[String, Int] = 3.asRight[String]
  // there are also additional syntax for bimap, and additional
  // ways to transform eithers with ensuring, handleError etc
}