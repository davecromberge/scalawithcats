package scalawithcats.monad

object State {
  // Allows us to pass state together with input to a
  // computation.  This allows us to get around using
  // mutation, and thread the state between composed
  // state functions. States are composed using normal
  // map and flatMap operations, as it forms a monad.

  import cats.instances.vector._
  import cats.syntax.applicative._
  import cats.data.State

  // State[S, A] is a type from S => (S, A)
  // e.g. State[Int, String] = i => (i, s"The int state is: $i")

  // A state function computes a result from an input state, and
  // transforms an input state into an output state.

  // Cats provides convenience functions that only operate on the
  // state or result, such as .get, .put, .modify, .pure, .inspect.
  // These are sequentially constructed within a for comprehension
  // to read like a normal imperative program.

  // For example, we can model a post-order expression calculator
  // in this manner.  Given the expression: 2 3 * 3 +, calculate
  // 2 * 3 + 3 = 9

  type CalcState[A] = cats.data.State[List[Int], A]

  def evalOne(sym: String): CalcState[Int] =
    sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }

  def evalAll(input: List[String]): CalcState[Int] = 
    input.foldLeft(0.pure[CalcState]) { (a, b) => 
      a.flatMap(_ => evalOne(b))
    }
  
  def operator(func: (Int, Int) => Int): CalcState[Int] =
    cats.data.State[List[Int], Int] {
      case b :: a :: tail =>
        val ans = func(a, b)
        (ans :: tail, ans)

      case _ =>
        sys.error("Fail!")
    }

  def operand(num: Int): CalcState[Int] = cats.data.State[List[Int], Int] { stack =>
    (num :: stack, num)
  }
}
