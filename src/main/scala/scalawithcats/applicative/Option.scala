package scalawithcats.applicative

object Option {
  // Applicatives extend Semigroupal and Functor.  This allows us to map
  // over the result of composing contexts.  They provide the alternate
  // encodings to join a context, as with a semigroup.

  // Cats models applicatives with a typeclass Apply, with one primitive
  // ap:
  def ap[F[_], A, B](ff: F[A => B])(fa: F[A]): F[B] = ???

  // the product combinator from semigroup is defined in terms of ap:
  // def product[F[_], A, B](fa: F[A], fb: F[B]): F[(A, B)] =
  //    ap(map(fa)(a => (b: B) => (a, b))(fb)

  // See the discussion on the hierarchy in terms of apply, cartesian
  // monad, and appllicative on page 254-255.  The more constraints we
  // place upon a data type,  the more gaurantees we have that it conforms
  // to our expectations about its behaviour.
}