package scalawithcats. monad

object MonadError {

  // Monad error abstracts over datatypes with properties similar to
  // Either that provide error handling.  It has all Monad capabilities,
  // in addition to:
  //
  //  - raising an error  (from ApplicativeError)
  // .- handling an error (from ApplicativeError)
  //  - ensuring that a predicate P: A => Boolean holds for F[A]
  //
  // MonadError has the following type parameters:
  //
  //    F - the effect type
  //    E - the error type

  import cats.instances.either._

  type ErrorOr[A] = Either[String, A]
  // F is bound to ErrorOr, and the error type is String 
  val monadError = cats.MonadError[ErrorOr, String]
  val success: ErrorOr[Int] = monadError.pure[Int](42)
  val failure: ErrorOr[Nothing] = monadError.raiseError("Oops")

  monadError.handleError(failure) {
    case "Oops" =>
      monadError.pure[Int](1)
    case other =>
      monadError.raiseError("Still not ok")
  }

  // the syntax for monadError allows for these to be called directly
  import cats.syntax.applicative._
  import cats.syntax.applicativeError._
  import cats.syntax.monadError._
  val success2 = 42.pure[ErrorOr]
  val failure2 = "Oops".raiseError[ErrorOr, Int]
}
