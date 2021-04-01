package chapter7

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._

object ResourceApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    resources
      .use {
        case (a, b, c) =>
          applicationLogic(a, b, c)
      }
  }

  // compose managed resoucres into a single Resource value
  val resources: Resource[IO, (DependencyA, DependencyB, DependencyC] =
    (resourceA, resourceB, resourceC).tupled

  val resourceA: Resource[IO, DependencyA] = ???
  val resourceB: Resource[IO, DependencyB] = ???
  val resourceC: Resource[IO, DependencyC] = ???

  // uses - does not manage
  def applicationLogic(a: DependencyA, b: DependencyB, c: DependencyC ): IO[ExitCode] = ???
}

trait DependencyA
trait DependencyB
trait DependencyC

/**
 * 1. The Resource data type captures the pattern where the code for state acquisition
 *    and release is separated from code that uses the state.
 *    A Resource can be composed
 *
 * 2. Use the resources in our IOApp, to acquire during execution of dependent code, and ensure they are released
 */
