package chapter3

/*
(par)sequence turns a nested structure “inside-out”
F[G[A]] => G[F[A]]
 */

import cats.effect._
import cats.implicits._
import lib.debug._

object ParSequence extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
//    val x: IO[List[Int]] = tasks.parSequence
    tasks.parSequence.debug.as(ExitCode.Success)
  }

  val numTasks = 100
  val tasks: List[IO[Int]] = List.tabulate(numTasks)(task)
  def task(id: Int): IO[Int] = IO(id).debug
}
