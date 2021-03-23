package chapter5

import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.implicits._
import lib.debug.DebugHelper

/**
 * Blocker is a small wrapper around an ExecutionContext.
 */

object Blocking extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    Blocker[IO].use { blocker =>
      withBlocker(blocker).as(ExitCode.Success)
    }

  def withBlocker(blocker: Blocker): IO[Unit] =
    for {
      _ <- IO("on default").debug
      _ <- blocker.blockOn(IO("on blocker").debug)
      _ <- IO("where am I?").debug
    } yield ()
}

// 1. create resource and use its blocker

// 2. Subsequent effects execute on the original context, not the blocking one
