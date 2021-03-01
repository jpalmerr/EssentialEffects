package chapter2

import cats.Applicative.ops._
import cats.effect._

object HelloWorld extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    helloWorld.as(ExitCode.Success)

  val helloWorld: IO[Unit] = IO(println("Hello world!"))
}

/**
 * The application entry point is the run method, which must return IO[ExitCode]
 *
 * Declare the computations that will be run
*/

