package scalawithcats.monad

object Reader {
  // Allows us to sequence functions that depend on some input.
  // The reader allows us to compose these types of functions
  // together.

  import cats.instances.vector._
  import cats.syntax.applicative._
  import cats.data.Reader

  // Trivial example of a reader for Cat => String 
  case class Cat(name: String)

  val catsReader: Reader[Cat, String] = cats.data.Reader(cat => cat.name)
  catsReader.run(Cat("Kai"))

  case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
  )

  type DbReader[A] = cats.data.Reader[Db, A]

  def findUserName(id: Int): DbReader[Option[String]] =
    cats.data.Reader(_.usernames.get(id))

  def checkLogin(username: String, password: String): DbReader[Boolean] =
    cats.data.Reader(_.passwords.get(username).fold(false)(_ == password))

  // Kleisli arrows provide a more general form of reader where the 
  // result type constructor is generalised: A => F[B]
}
