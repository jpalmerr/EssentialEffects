package chapter7

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.implicits._
import lib.debug.DebugHelper

import scala.concurrent.duration.DurationInt

object ResourceBackgroundTask extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- backgroundTask.use { _ =>
        IO.sleep(200.millis) *> IO("$s is so cool!").debug // sleep to ensure background task does some work
      }
      _ <- IO("done!").debug
    } yield ExitCode.Success

  val backgroundTask: Resource[IO, Unit] = {
    val loop: IO[Unit] = (IO("looping...").debug *> IO.sleep(100.millis)).foreverM

    /*
    Resource
      .make(IO("> forking backgroundTask").debug *> loop.start)( // fork a fiber
        IO("< canceling backgroundTask").debug.void *> _.cancel // release it
      )
      .void
     */

    loop.background.void

  }
}

/*
our effect is forked as a fiber

once the use effect finishes ($s is so cool!)

the fiber is cancelled (< canceling backgroundTask)
 */
