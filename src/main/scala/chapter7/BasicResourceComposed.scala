package chapter7

import cats.effect._
import cats.implicits._
import lib.debug.DebugHelper


object BasicResourceComposed extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
  (stringResource, intResource).tupled
    .use {
      case (s, i) =>
        IO(s"$s is so cool!").debug *> IO(s"$i is also cool!").debug
    }
    .as(ExitCode.Success)

  val stringResource: Resource[IO, String] = Resource.make(
    IO("> acquiring stringResource").debug *> IO("String")
  )(_ => IO("< releasing stringResource").debug.void)


  val intResource: Resource[IO, Int] = Resource.make(
    IO("> acquiring intResource").debug *> IO(99) )(_ => IO("< releasing intResource").debug.void)
}
