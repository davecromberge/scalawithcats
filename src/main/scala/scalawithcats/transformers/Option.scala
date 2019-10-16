package scalawithcats.transformers

package scalawithcats.transformers

object Option {
  // Monad transformers allow us to use component monads without
  // having to pack and unpack every layer.  This allows us to
  // wrap stacks of effects together to form a new Monad.
  // Monad transformers represent the inner monad in a stack,
  // whilst the first type parameter represents the outer monad. 

  // A monad transformer where the inner monad is an Option for
  // any monad M[_] on the outer layer.
  import cats.data.OptionT
  import cats.data.EitherT
  import cats.instances.list._ // For Monad
  import cats.syntax.applicative._ // For pure
  import scala.concurrent.Future

  type ListOption[A] = OptionT[List, A]

  val result1: ListOption[Int] = 32.pure[ListOption]
  val result2: ListOption[Int] = 10.pure[ListOption]

  result1.flatMap { r1 =>
    result2.map(r2 => r1 + r2)
  }
}