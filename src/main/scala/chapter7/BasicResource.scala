package chapter7

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import lib.debug.DebugHelper

object BasicResource extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    stringResource
      .use { s =>
        IO(s"$s is so cool").debug
      }
      .as(ExitCode.Success)
  }

  val stringResource: Resource[IO, String] = Resource.make(
    IO("> acquiring stringResource").debug *> IO("String")
  )(_ => IO("< releasing stringResource").debug.void)
}

// we first acquire the value, then it is used, and then it is released.


object BasicResourceFailure extends IOApp { def run(args: List[String]): IO[ExitCode] =
  stringResource
    .use(_ => IO.raiseError(new RuntimeException("oh noes!")))
    .attempt
    .debug
    .as(ExitCode.Success)

  val stringResource: Resource[IO, String] = Resource.make(
    IO("> acquiring stringResource").debug *> IO("String") )(_ => IO("< releasing stringResource").debug.void)
}