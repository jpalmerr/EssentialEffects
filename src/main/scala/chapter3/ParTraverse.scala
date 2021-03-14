package chapter3

/* parTraverse is the parallel version of traverse; both have the type signature:
   F[A] => (A => G[B]) => G[F[B]]
*/

// List[A] => (A => IO[B]) => IO[List[B]]

/*

The most common use case of (par)traverse is when you have a collection of work to be done,
and a function which handles one unit of work.
Then you get a collection of results combined into one effect:
 */

import cats.effect._
import cats.implicits._
import lib.debug.DebugHelper

object ParTraverse extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    tasks.parTraverse(task).debug.as(ExitCode.Success)

  val numTasks = 100
  val tasks: List[Int] = List.range(0, numTasks)

  def task(id: Int): IO[Int] = IO(id).debug
}

/*
You can also think of (par)traverse as a variation of (par)mapN where results are
collected, but where every input effect has the same output type
 */