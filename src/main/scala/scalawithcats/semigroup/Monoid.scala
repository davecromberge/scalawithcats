package scalawithcats.semigroup

object Monoid {

  import cats.instances.int._

  // a monoid for a type A consists of the type instance for A together
  // with an associative combine operation, together with an identity
  // element for A.
  val intAdditionMonoid = cats.Monoid[Int]

  // semigroup syntax for combine can be imported from cats.syntax.semigroup._
  import cats.syntax.semigroup._
  val result = 1 |+| 3

  // monoids are useful in divide-and-conquer contexts from big data, and
  // also in distributed systems.
}

object MonoidInstances {

  val orMonoid: cats.Monoid[Boolean] = new cats.Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x || y
  }
  val andMonoid: cats.Monoid[Boolean] = new cats.Monoid[Boolean] {
    override def empty: Boolean = false
    override def combine(x: Boolean, y: Boolean): Boolean = x && y
  }
  val xorMonoid: cats.Monoid[Boolean] = new cats.Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x | y
  }
  val xandMonoid: cats.Monoid[Boolean] = new cats.Monoid[Boolean] {
    override def empty: Boolean = true
    override def combine(x: Boolean, y: Boolean): Boolean = x & y
  }
  def intersectionMonoid[A]: cats.Monoid[Set[A]] = new cats.Monoid[Set[A]] {
    override def empty: Set[A] = Set.empty[A]
    override def combine(x: Set[A], y: Set[A]): Set[A] = x intersect y
  }
  def unionMonoid[A]: cats.Monoid[Set[A]] = new cats.Monoid[Set[A]] {
    override def empty: Set[A] = Set.empty[A]
    override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
  }
}

object MonoidLaws {
  import cats.Monoid

  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean =
    (m.combine(x, m.empty) == x) && (m.combine(m.empty, x) == x)

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean =
    m.combine(m.combine(x, y), z) == m.combine(x, m.combine(y, z))
}