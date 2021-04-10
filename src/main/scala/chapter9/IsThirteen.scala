package chapter9

/*
On one hand if we poll too often we’re being inefficient,
on the other hand if we poll too infrequently then our reaction to the state change is delayed

Instead of having to guess a polling interval,
we can push this responsibility behind an abstraction that will, from the outside,
block subsequent execution until the condition is fulfilled
 */

import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import lib.debug.DebugHelper

import scala.concurrent.duration._

object IsThirteen extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      ticks <- Ref[IO].of(0L)
      is13  <- Deferred[IO, Unit]
      _ <- (beepWhen13(is13), tickingClock(ticks, is13)).parTupled
    } yield ExitCode.Success

  def beepWhen13(is13: Deferred[IO, Unit]): IO[Unit] =
    for {
      _ <- is13.get
      _ <- IO("BEEP!").debug
    } yield ()

  def tickingClock(ticks: Ref[IO, Long], is13: Deferred[IO, Unit]): IO[Unit] =
    for {
      _         <- IO.sleep(1.second)
      _         <- IO(System.currentTimeMillis).debug
      count     <- ticks.get
      newCount  <- ticks.set(count + 1)
      _         <- if (newCount >= 13) is13.complete(()) else IO.unit
      _         <- tickingClock(ticks, is13)
    } yield ()
}
