import cats.effect._
import cats.implicits._

/**
 * parMapN is the parallel version of the applicative mapN method.
 * It lets us combine multiple effects into one, in parallel, by specifying how to combine the outputs of the effects:
 */


object xyz extends IOApp {
  type A
  type B
  type C

  val ia: IO[A] = IO(???)
  val ib: IO[B] = IO(???)
  def f(a: A, b: B): C = ???
  val ic: IO[C] = (ia, ib).parMapN(f)

  override def run(args: List[String]) = ???
}
