package scalawithcats.ch1

import cats.Show

import java.util.Date

object ShowIntro {
  // toString variant
  def showInt: Show[Int] = Show.fromToString[Int]
  // function variant
  def showInt2: Show[Int] = Show.show(i => i.toString)
  // a custom implementation for show
  def showDate: Show[Date] = Show.show(date =>
    s"${date.getTime}ms since the Epoch"
  )
}

object DefaultInstances {
  // import the instance of show for int
  import cats.instances.int._
  // import the extension method show for int
  import cats.syntax.show._

  val three = 3.show
}
