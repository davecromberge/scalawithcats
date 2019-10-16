package scalawithcats.semigroup

object SuperAdder {
  import cats.{Monoid, Semigroup}
  import cats.syntax.semigroup._

  case class Order(totalCost: Double, quantity: Double)

  def add[A](items: List[A])(implicit semigroup: Semigroup[A]): Option[A] =
    Semigroup[A].combineAllOption(items)

  def add[A](items: List[A])(implicit monoid: Monoid[A]): A =
    Monoid[A].combineAll(items)
}