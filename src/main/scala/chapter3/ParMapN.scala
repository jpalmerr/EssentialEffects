package chapter3


import cats.effect._
import cats.implicits._
import lib.debug._


object ParMapN extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    par.as(ExitCode.Success)

  val hello = IO("hello").debug
  val world = IO("world").debug

  val par = (hello, world)
    .parMapN((h, w) => s"$h $w")
    .debug
}

/**
 * Notice different threads are used
 * The execution order of parallel tasks is non-deterministic
 */