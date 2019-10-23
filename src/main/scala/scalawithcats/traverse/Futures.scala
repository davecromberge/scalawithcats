package scalawithcats.traverse

object Futures {
  // Traverse leverages applicatives to provide a convenient and lawful pattern
  // for iteration.  This is more convenient than providing accumulators and
  // combining functions as we do when using folds.
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  import cats.Applicative
  import cats.syntax.apply._
  import cats.syntax.applicative._
  import cats.instances.future._

  val hosts = List("a", "b", "c")
  def getUptime(host: String): Future[Int] = ???

  // this uses the mapN syntax from apply
  def traverse(acc: Future[List[Int]], host: String) =
    (acc, getUptime(host)).mapN(_ :+ _)

  // defined more generally for any Applicative[F], for List sequences:
  import scala.language.higherKinds

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(f: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (b, a) =>
      (b, f(a)).mapN(_ :+ _)
    }

  def listSequence[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] =
    listTraverse(list)(identity)

  // When using monads during traversal, the semigroupal mapN combine
  // function obeys the same semantics defined for the monad and its
  // sequencing via flatMap.  This means that sequence a list of
  // options will produce None if any are empty, and a vector will
  // produce the cross product.

  // Cats provides traverse and sequence functions for any sequence type
  // Traverse[F], with Applicative[G]:
  import cats.Traverse
  import cats.instances.list._
  import cats.instances.future._
  Traverse[List].sequence(List(Future.successful(1), Future.successful(2)))
}