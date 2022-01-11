package chapter3

import cats.effect._
import cats.implicits._
import lib.debug._

import scala.concurrent.duration.DurationInt

object ParMapNErrors extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    e1.attempt.debug *> IO("---").debug *> e2.attempt.debug *> IO("---").debug *> e3.attempt.debug *> IO.pure(ExitCode.Success)
    // attempt transforms an IO[A] into an IO[Either[Throwable, A]]
  }


  val ok = IO("hi").debug


  val ko1 = IO.raiseError[String](new RuntimeException("oh!")).debug
  val ko2 = IO.raiseError[String](new RuntimeException("noes!")).debug


  val e1 = (ok, ko1).parMapN((_, _) => ())
  val e2 = (ko1, ok).parMapN((_, _) => ())
  val e3 = (ko1, ko2).parMapN((_, _) => ())
}

/*

[ioapp-compute-1] hi
[ioapp-compute-2] Left(java.lang.RuntimeException: oh!)
[ioapp-compute-2] ---
[ioapp-compute-3] Left(java.lang.RuntimeException: oh!)
[ioapp-compute-3] ---
[ioapp-compute-5] Left(java.lang.RuntimeException: oh!)

 */

object ParMapNErrorsDelay extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    e1.attempt.debug *> IO("---").debug *> e2.attempt.debug *> IO("---").debug *> e3.attempt.debug *> IO.pure(ExitCode.Success)
    // attempt transforms an IO[A] into an IO[Either[Throwable, A]]
  }


  val ok = IO("hi").debug


  val ko1 = IO.sleep(1.second).as("ko1").debug *> IO.raiseError[String](new RuntimeException("oh!")).debug
  val ko2 = IO.raiseError[String](new RuntimeException("noes!")).debug


  val e1 = (ok, ko1).parMapN((_, _) => ())
  val e2 = (ko1, ok).parMapN((_, _) => ())
  val e3 = (ko1, ko2).parMapN((_, _) => ())
}

/*

[ioapp-compute-1] hi
[ioapp-compute-2] ko1
[ioapp-compute-2] Left(java.lang.RuntimeException: oh!)
[ioapp-compute-2] ---
[ioapp-compute-3] hi
[ioapp-compute-4] ko1
[ioapp-compute-4] Left(java.lang.RuntimeException: oh!)
[ioapp-compute-4] ---
[ioapp-compute-5] Left(java.lang.RuntimeException: noes!)

 */

/**
 * We’ve delayed ko1, so for e1 we see the output of ok and ko1 before ko1 triggers the exception.
 *
 * For e2, even though ko1 is the first argument to parMapN we see the same output as e1
 *
 * For e3 we see the output of ko2, since ko1 was delayed and thus executed after ko2
 *
 * The first failure to happen is used as the failure of the composed effect
 */