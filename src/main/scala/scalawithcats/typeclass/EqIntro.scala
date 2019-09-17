package scalawithcats.ch1

import cats.Eq
import cats.instances.int._
import cats.instances.long._
import cats.instances.option._
import cats.instances.string._
import cats.syntax.eq._
import cats.syntax.option._

import java.util.Date

// Eq defines type safe equality between instances of
// any given type.
object EqIntro {

  val cmp = 1 === 3
  val cmp2 = Eq[Int].eqv(1, 3)
  val cmp3 = (Some(1): Option[Int]) === (None: Option[Int])
  // special syntax to avoid typing arguments to === as Option as in cmp3
  val cmp4 = 1.some === none[Int]
}

object EqInstances {
  implicit val eqDate: Eq[Date] = Eq.instance((date1, date2) => date1.getTime === date2.getTime)

  implicit val eqCat: Eq[Cat] =
    Eq.instance((cat1, cat2) => cat1.age === cat2.age && cat1.name === cat2.name && cat2.colour === cat2.colour)
}
