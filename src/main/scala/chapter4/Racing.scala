package chapter4

import cats.Applicative.ops._
import cats.effect._
import cats.effect.implicits._
import lib.debug.DebugHelper

import scala.concurrent.duration._

/**
 * If an error occurs during one of our effects, we need to cancel "the other" fiber
 * -> join.OnError(...)
 *
 * what if they fail in the "wrong" order, we'd have to wait
 *
 * we need to avoid doing a join on a potentially cancelled effect,
 * but here either effect could be cancelled first— we don’t know which
 *
 *so lets race two effects, whichever finishes first can join the other effect
 */

object Racing extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      done <- IO.race(task, timeout) // IO.race races two effects, and returns the value of the first to finish. The loser of the race is cancelled.
      _ <- done match {
        case Left(_) => IO(" task: won").debug
        case Right(_) => IO("timeout: won").debug
      }
    } yield ExitCode.Success

  val task: IO[Unit] = annotatedSleep(" task", 100.millis)
  val timeout: IO[Unit] = annotatedSleep("timeout", 500.millis)

  def annotatedSleep(name: String, duration: FiniteDuration): IO[Unit] = (
    IO(s"$name: starting").debug *>
      IO.sleep(duration) *>
      IO(s"$name: done").debug
    ).onCancel(IO(s"$name: cancelled").debug.void).void
}