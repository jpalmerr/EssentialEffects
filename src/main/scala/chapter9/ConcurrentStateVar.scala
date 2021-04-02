package chapter9

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import lib.debug.DebugHelper

import scala.concurrent.duration._

object ConcurrentStateVar extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- (tickingClock, printTicks).parTupled // We start the two effects in parallel.
    } yield ExitCode.Success

  var ticks: Long = 0L // We use a var to hold our state

  val tickingClock: IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      _ = (ticks = ticks + 1)
      _ <- tickingClock
  } yield ()

  val printTicks: IO[Unit] = for {
    _ <- IO.sleep(5.seconds)
    _ <- IO(s"TICKS: $ticks").debug.void
    _ <- printTicks
  } yield ()

}

// atomic state has no substates

object ConcurrentStateRef extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      ticks <- Ref[IO].of(0L) //  We create a new Ref initialized to 0. Creating a Ref is also an effec
      _ <- (tickingClock(ticks), printTicks(ticks)).parTupled // share the Ref with both effects that will run in parallel
    } yield ExitCode.Success

  def tickingClock(ticks: Ref[IO, Long]): IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(System.currentTimeMillis).debug
      _ = ticks.update(_ + 1) //  update method to atomically update the state in ticks
      _ <- tickingClock(ticks)
  } yield ()

  def printTicks(ticks: Ref[IO, Long]): IO[Unit] = for {
    _ <- IO.sleep(5.seconds)
    _ <- IO(s"TICKS: ${ticks.get}").debug.void // We access the current state with the get method
    _ <- printTicks(ticks)
  } yield ()
}
