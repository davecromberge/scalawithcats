package scalawithcats.semigroupal

object Validated {
  // Since Either is a monad in cats, using Semigroupal[Either] does not accumulate
  // errors.  Instead a datastructure called Validated is provided, that extends
  // Semigroupal, but is not a monad.

  import cats.Semigroupal
  import cats.data.Validated
  import cats.syntax.apply._
  import cats.instances.list._
  import cats.instances.string._

  type AllErrorsOr[A] = Validated[String, A]

  val all = Semigroupal[AllErrorsOr].product(
    cats.data.Validated.invalid("error 1"),
    cats.data.Validated.invalid("error 2")
  ) // Invalid(List("error 1", "error 2"))

  // Validated has two subtypes: Valid and Invalid, corresponnding to
  // Either's left and right. Importing the validated syntax allows for
  // us to create instances from values:
  import cats.syntax.validated._

  123.valid[List[String]]   // cats.data.Validated[List[String],Int]

  // We can also use syntax from Applicative and ApplicativeError to create
  // instances:
  import cats.syntax.applicative._
  123.pure[AllErrorsOr]

  import cats.syntax.applicativeError._
  "error 1".raiseError[AllErrorsOr, Int]

  // There are also helper methods on the companion object to create
  // instances from try, either, exceptions etc:
  cats.data.Validated.catchNonFatal(sys.error("error"))
  cats.data.Validated.fromEither(Left("error"))
  cats.data.Validated.fromOption(None, "error")

  // Combining instances of validated, Validates accumulates errors
  // using a Semigroupal defined for the error type.
  ("error1".invalid[Int], "error2".invalid[Int]).tupled

  // It is also possible to map, bimap and use .andThen (instead of flatMap)
  // over the Validated data type.  Moreover, it is possible to convert
  // to and from eithers by using syntax on either.

  type RequestValidation[A] = Validated[List[String], A]

  def getValue[A](input: Map[String, String], key: String): String =
    input.getOrElse(key, "")

  def parseInt(input: String): Validated[List[String], Int] =
    cats.data.Validated.catchNonFatal(input.toInt).leftMap(e => List(e.getMessage))

  def nonBlank(name: String, input: String): RequestValidation[String] =
    if (input.trim.isEmpty)
      List(s"$name is blank").invalid
    else
      input.valid[List[String]]

  def nonNegative(name: String, input: String): RequestValidation[Int] =
    parseInt(input).andThen { i =>
     if (i < 0)
      List(s"$name is negative").invalid
    else
      i.valid[List[String]]
    }

  def readName(input: Map[String, String]): RequestValidation[String] =
    nonBlank("name", getValue(input, "name"))

  def readAge(input: Map[String, String]): RequestValidation[Int] =
    nonBlank("age", getValue(input, "age")).andThen(a => nonNegative("age", a))

  def validate(input: Map[String, String]): Validated[List[String], (String, Int)] =
    (readName(input), readAge(input)).tupled
}