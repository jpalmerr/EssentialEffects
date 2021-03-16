package chapter4

/**
 * def cancel: cats.effect.CancelToken[IO]
 * type CancelToken[F[_]] = F[Unit]
 *
 * Canceling a Fiber is itself an effect. It produces a Unit value once the effect is canceled
 */

import cats.Applicative.ops._
import cats.effect._
import cats.effect.implicits._
import lib.debug.DebugHelper

object Cancel extends IOApp {

  def run(args: List[String]): IO[ExitCode] = for {
    fiber <- task.onCancel(IO("i was cancelled").debug.void).start
    _ <- IO("pre-cancel").debug
    _ <- fiber.cancel
    _ <- IO("canceled").debug
  } yield ExitCode.Success

  val task: IO[String] = IO("task").debug *> IO.never // never complete
}

/*
Invoking cancel more than once has the same effect as invoking it once—a canceled task will continue to be canceled.
However, if you join after you cancel, the join will never finish, because no result will ever be produced.
 */
