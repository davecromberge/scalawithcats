package scalawithcats.monad

object Writer {
  // Allows us to carry a log along with a computation.
  // This is useful in a concurrent context, because
  // the log travels with the value and is not interleaved
  // across different computations. 

  type CatsWriter[W, A] = cats.data.WriterT[cats.Id, W, A]

  import cats.instances.vector._
  import cats.syntax.applicative._
  import cats.syntax.writer._

  type Logged[A] = CatsWriter[Vector[String], A]
  
  // If we have a result and no log
  val w1 = 123.pure[Logged]  // cats.data.WriterT(Vector(), 123)
  // If we have a log and no result
  val w2 = Vector("msg1", "msg2").tell // cats.data.WriterT(Vector("msg1", "msg2"), ())
  // If we have both a log and a result
  val w3 = 123.writer(Vector("msg1", "msg2")) // cats.data.WriterT(Vector("msg1", "msg2"), 123)

  // extracting the values can be done via .written or .value:
  val log1 = w1.written
  val value1 = w1.value

  // or, via run:
  val (log2, value2) = w2.run

  // The log is preserved when composing writers, and flatMap will append the logs
  // in sequence.  Therefore it is important to choose a datastructure with efficient
  // append access.
  // The log may also be transformed with .mapWritten, and the result and log transformed
  // with .mapBoth or .bimap.

}
