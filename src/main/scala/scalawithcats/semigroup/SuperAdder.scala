package scalawithcats.semigroup

import cats.{Monoid, Semigroup}
import cats.syntax.semigroup._

object SuperAdder {
  case class Order(totalCost: Double, quantity: Double)

  def add[A](items: List[A])(implicit semigroup: Semigroup[A]): Option[A] =
    Semigroup[A].combineAllOption(items)

  def add[A](items: List[A])(implicit monoid: Monoid[A]): A =
    Monoid[A].combineAll(items)
}