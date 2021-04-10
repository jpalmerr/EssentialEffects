package chapter9

import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import lib.debug.DebugHelper

trait CountdownLatch {
  def await(): IO[Unit]
  def decrement(): IO[Unit]
}

object CountdownLatch {
  def apply(n: Long)(implicit cs: ContextShift[IO]): IO[CountdownLatch] =
    for {
      // setup:
      whenDone <- Deferred[IO, Unit]
      state    <- Ref[IO].of[State](Outstanding(n, whenDone))
    } yield new CountdownLatch {
      override def await(): IO[Unit] =
        state.get.flatMap {
          case Outstanding(_, whenDone) => whenDone.get
          case Done() => IO.unit
        }

      override def decrement(): IO[Unit] =
        state.modify {
          case Outstanding(1, whenDone) => Done() -> whenDone.complete(())
          case Outstanding(n, whenDone) => Outstanding(n - 1, whenDone) -> IO.unit
          case Done() => Done() -> IO.unit
        }.flatten
    }

  sealed trait State
  final case class Outstanding(n: Long, whenDone: Deferred[IO, Unit]) extends State
  final case class Done() extends State
}

object LatchExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      latch <- CountdownLatch(1)
      _     <- (actionWithPrerequisites(latch), runPrerequisite(latch)).parTupled
    } yield ExitCode.Success

  // writer is fulfilling one or more prerequisites
  def runPrerequisite(latch: CountdownLatch) =
    for {
      _ <- IO("waiting for prerequisites (run)").debug
      _ <- latch.await()
      result <- IO("action").debug
    } yield result

  // reader waiting for latch to open
  def actionWithPrerequisites(latch: CountdownLatch) =
    for {
      _      <- IO("waiting for prerequisites (action)")
      _      <- latch.await()
      result <- IO("action").debug
    } yield result
}

/**
 * two states:
 * n oustanding decrement() ops to expect OR done
 *
 * when a "reader" calls await()
 *  - if our state is Outstanding, block the caller via whenDone
 *  - Done => do nothing
 *
 *  when a "writer" calls decrement
 *  - if our state is Outstanding
 *    - if n is 1 transition to Done and unblock via whenDone
 *    - otherwise decrement n
 *
 *  - Done => do nothing
 *
 *
 */

