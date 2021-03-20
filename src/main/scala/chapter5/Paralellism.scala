package chapter5

import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import lib.debug.DebugHelper

object Paralellism extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(s"number of CPUs: $numCpus").debug
      _ <- tasks.debug
    } yield ExitCode.Success

  val numCpus = Runtime.getRuntime().availableProcessors() // thread pools
  val tasks = List.range(0, numCpus * 2).parTraverse(task)
  def task(i: Int): IO[Int] = IO(i).debug
}

/**
 * The answer is that when we compose effects in parallel,
 * during execution each effect is only scheduled to be executed,
 * and a separate asynchronous process is responsible for executing the scheduled effects on an available thread.
 *
 * When a thread finishes its work, another effect is executed on it.
 *
 * In Scala this exactly maps to an ExecutionContext,
 * which encapsulates both a queue of scheduled tasks and a set of threads used to execute them
 */

