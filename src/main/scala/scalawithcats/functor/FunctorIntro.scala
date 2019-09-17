package scalawithcats.functor

import cats.Functor
import cats.instances.function._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.functor._ // for map

object FunctorIntro {

  // Structure preserving map operation.  The map removes the context
  // before function application and then repackages the result back
  // into that context.  This is different from an Applicative that
  // applies a function in the context directly, which is more 
  // powerful.
  // def map[A, B](fa: F[A])(f: A => B): F[B]
  // def apply[A, B](fa: F[A])(f: F[A => B]): F[B]
  //
  // type F[_] is a higher-kinded type, which is like a type for
  // types.  F here is a type constructor, that, when applied to
  // a type will form a proper type. 

  // Function1's are also functors, in that mapping over a function1
  // is the same as composing two functions f and g

  val func1: Int => Double = _.toDouble
  val func2: Double => Double = Math.pow(_, 2)
  // composition with map is a way to sequence operations, where
  // providing an argument to the resulting function will apply
  // each function in sequence.
  val composed = func1.map(func2)
  val composed2 = func1 andThen func2

  val list = List.fill(10)("a")
  val result = Functor[List].map(list)(_.toUpperCase)

  // Functor provides lift as a derived combinator
  // def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)
  val func3: Int => Int = x => x * x
  val lifted: Option[Int] => Option[Int] = Functor[Option].lift(func3)
  lifted(Some(2))

  // Ordinarily, covariant functors append a function to a chain.
  // Contravariant functors prepend a function to a chain via contramap.
  // This only makes sense in the case of transformations.
  trait Printable[A] { self =>
    def format(a: A): String = ???
    def contramap[B](f: B => A): Printable[B] = new Printable[B] {
      override def format(b: B): String = self.format(f(b))
    }
  }

  // demonstrates how to use contramap, to get an F[B], when
  // we have a transformation from B => A
  final case class Box[A](a: A)
  object Box {
    implicit def boxPrintable[A](implicit P: Printable[A]): Printable[Box[A]] = P.contramap(_.a)
  }

  // Invariant functors define a function imap that is equivalent to
  // a combination of map and contramap.  If we have a F[A] and a pair
  // of functions A => B and b => A, we can define a F[B]:
  trait Codec[A] { self =>
    def encode(a: A): String
    def decode(s: String): A
    def imap[B](contramap: B => A, map: A => B): Codec[B] = new Codec[B] {
      override def encode(b: B): String = self.encode(contramap(b))
      override def decode(s: String): B = map(self.decode(s))
    }
  }

  object CodecInstances {
    implicit val stringCodec: Codec[String] = new Codec[String] {
      override def decode(s: String): String = s
      override def encode(a: String): String = a
    }
    implicit val intCodec: Codec[Int] = stringCodec.imap(_.toString, _.toInt)
  }
}

// Cats uses implicit classes to add the capabilities of functor as
// syntax for any type A where a functor instance is available
object FunctorSyntax {
  implicit class FunctorOps[F[_], A](src: F[A]) {
    def map[B](f: A => B)(implicit F: Functor[F]): F[B] = F.map(src)(f)
  }
}