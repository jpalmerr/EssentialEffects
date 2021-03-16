package chapter4

import cats.Applicative.ops._
import cats.effect._
import lib.debug.DebugHelper

import scala.concurrent.duration._

/**
 * The first thing we can do is to join it, which will return the result of the forked IO effect.
 * We’re giving up the control the fiber gave us
 * -> we can only talk about the eventual result of the previously- forked value.
 */

/**
 * def join: F[A]
 *
 * Returns a new task that will await for the completion of the
 * underlying fiber, (asynchronously) blocking the current run-loop
 * until that result is available.
 */

object JoinAfterStart extends IOApp {

  def run(args: List[String]): IO[ExitCode] = for {
    fiber <- task.start
    _ <- IO("pre-join").debug
    _ <- fiber.join.debug
    _ <- IO("post-join").debug
  } yield ExitCode.Success

  val task: IO[String] = IO.sleep(2.seconds) *> IO("task").debug
}
