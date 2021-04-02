package chapter4

import cats.effect._
import lib.debug.DebugHelper

/**
 * in a for expression for example, we only continue once result is available
 * instead of waiting, we could for an effect
 *
 * the effect will be started but we aren't interested in waiting for its completion
 */

object Start extends IOApp {

  def run(args: List[String]): IO[ExitCode] = for {
    _ <- task.start
    _ <- IO("task was started").debug
  } yield ExitCode.Success

  val task: IO[String] = IO("task").debug
}

/*
def start(implicit cs: ContextShift[IO]): IO[Fiber[IO, A]]

context shift is our thread pool
fiber is a data type  which lets us act on the started effect
wrapped in an IO, our original IO is not running until we explicitly run it, so we need to delay access
 */

