package scalawithcats.ch1

/**
  * Type classes allow us to extend existing functionality without
  * resorting to inheritance.  This can be useful when the original
  * source code cannot be modified.
  * They consist of three parts:
  * 1) Type class itself
  * 2) Instances of the type class for particular types
  * 3) Interface to expose to users (syntax)
  */
sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json

// A type class is usually represented by a trait with one type
// parameter.
trait JsonWriter[A] {
  def write(a: A): Json
}

object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    override def write(a: String): Json = JsString(a)
  }
  implicit val numberWriter: JsonWriter[Double] = new JsonWriter[Double] {
    override def write(a: Double): Json = JsNumber(a)
  }
  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(a: Person): Json =
      JsObject(Map("name" -> stringWriter.write(a.name), "email" -> stringWriter.write(a.email)))
  }
  // The compiler can combine implicits when searching for implicit instances.  This is known as
  // recursive implicit resolution.  This is different for a def without implicit arguments which
  // are known as implicit conversion functions.
  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = new JsonWriter[Option[A]] {
    override def write(a: Option[A]): Json = a match {
      case Some(v) => writer.write(v)
      case None => JsNull
    }
  }
}

// The interface for our typeclass can be provided in two ways:
// 1) interface objects place all methods within a singleton object
// 2) interface syntax extends existing types with interface methods i.e. syntax

// Interface object technique requires implicit to be in scope
object Json { 
  def toJson[A](a: A)(implicit writer: JsonWriter[A]): Json =
    writer.write(a)
}

// Syntax technique is used by cats and uses extension methods
object JsonSyntax {
  implicit class JsonWriterOps[A](a: A) {
    def toJson(implicit writer: JsonWriter[A]): Json =
      writer.write(a)
  }
}

// Use implicitly[JsonWriter[Person]] in normal code flow to determine
// whether the compiler can locate an implicit instance of JsonWriter for Person.

// The compiler searches for candidate instances in the implicit scope at the
// call site, which roughly consists of:
// - local or inherited definitions
// - imported definitions;
// - definitions in the companion object of the type class or the parameter type

// How do we control which instance of a typeclass is selected when there are multiple
// options available in scope? Consider Some[Int] === None for Eq.
// We could add variance to our Eq typeclass:
// -  Invariance: neither the supertype or subtype instance is used
// -  Contra-variance: Supertype instance can be used e.g PrintWriter[Shape] instead
//    of PrintWriter[Circle] since a circle is a shape and this is a legal
//    substitution.
// -  Invariance: Subtype instance will be used e.g. Some[Int]