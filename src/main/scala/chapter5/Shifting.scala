package chapter5

import cats.effect.{ExitCode, IO, IOApp}
import lib.debug.DebugHelper

// long-running effects

/*
Consider a recursive effect, like a ticking clock
To hoard the current thread for such an effect would make one less thread available
=> reducing capability of work our app can do

To ensure a recursive loop doesn’t steal a thread and never give it back,
we’d like to be able to declare, as an effect itself, “reschedule the remainder of the computation”
known as a "asynchronous boundary"

We can produce this using IO.shift
 */

object Shifting extends IOApp{
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO("one").debug
      _ <- IO.shift
      _ <- IO("two").debug
      _ <- IO.shift
      _ <- IO("three").debug } yield ExitCode.Success
}

/*
[ioapp-compute-0] one
[ioapp-compute-1] two
[ioapp-compute-2] three
 */

/*
effects that need to be executed in neither the default nor blocking context

Interestingly
Cats Effect inserts one at runtime every 512 flatMap calls. This is a kind of fail-safe
 */