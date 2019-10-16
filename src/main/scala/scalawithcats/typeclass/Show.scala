package scalawithcats.ch1

import java.util.Date

object Show {
  // toString variant
  def showInt: cats.Show[Int] = cats.Show.fromToString[Int]
  // function variant
  def showInt2: cats.Show[Int] = cats.Show.show(i => i.toString)
  // a custom implementation for show
  def showDate: cats.Show[Date] = cats.Show.show(date =>
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
