package scalawithcats.transformers

package scalawithcats.transformers

object Either {
  import cats.data.EitherT
  import cats.data.OptionT
  import cats.implicits._
  import cats.instances.future._
  import cats.instances.int._
  import cats.instances.either._
  import cats.instances.option._
  import cats.syntax.applicative._ // For pure
  import cats.syntax.flatMap._
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  // monad stacks are built from the inside out, for example,
  // a Future of an Either of error string or Option:
  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  // The kind projector compiler plugin may be used in place
  // of type aliases to partially apply type constructors 
  // e.g. 123.pure[EitherT[Future, String, ?]]

  // Stacks may be unpacked using the .value field on a transformer:
  type ErrorOr[A] = Either[String, A] 
  type ErrorType[A] = OptionT[ErrorOr, A]

  // Many monads in cats are defined in terms of the corresponding
  // monad transformer and the identity monad.  Since monad
  // stacks do not work well in a large heterogenous code base,
  // it is recommended to expose the untransformed stack at
  // api boundaries and use transformer stacks within a bounded
  // context.

  /*
  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map("Jazz" -> 6, "Bumblebee" -> 8, "Hot Rod" -> 10)

  def getPowerLevel(autoBot: String): Response[Int] =
    powerLevels.get(autoBot) match {
      case Some(level) =>
        EitherT.right(Future.successful(level))
      case None =>
        EitherT.left(Future.successful(s"Could not find bot: $autoBot"))
    }
  */
}