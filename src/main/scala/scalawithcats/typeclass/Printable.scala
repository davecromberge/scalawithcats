package scalawithcats.ch1

trait Printable[A] {
  def format(a: A): String
}

object Printable {
  def format[A](a: A)(implicit P: Printable[A]): String =
    P.format(a)
  def print[A](a: A)(implicit P: Printable[A]): Unit =
    println(format(a))
}

object PrintableSyntax {
  implicit class PrintableOps[A](a: A) {
    def format(implicit P: Printable[A]): String =
      P.format(a)
    def print(implicit P: Printable[A]): Unit =
      println(format)
  }
}

object PrintableInstances {
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(a: String): String = a
  }
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }
}

final case class Cat(name: String, age: Int, colour: String)

object Cat {
  import PrintableInstances._
  import cats.Show
  import cats.instances.string._
  import cats.instances.int._
  import cats.syntax.show._

  implicit def printableCat: Printable[Cat] = new Printable[Cat] {
    override def format(a: Cat): String = {
      val name = Printable.format(a.name)
      val age = Printable.format(a.age)
      val colour = Printable.format(a.colour)
      s"$name is a $age year-old $colour cat."
    }
  }

  implicit val showCat: Show[Cat] = Show(cat =>
    s"${cat.name.show} is a ${cat.age.show} year-old ${cat.colour.show} cat."
  )
}