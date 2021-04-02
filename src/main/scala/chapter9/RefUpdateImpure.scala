package chapter9

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._


object RefUpdateImpure extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    for {
      ref <- Ref[IO].of(0)
      _ <- List(1, 2, 3).parTraverse(task(_, ref)) // We “race” three tasks updating the same Ref in parallel via parTraverse
    } yield ExitCode.Success

  def task(id: Int, ref: Ref[IO, Int]): IO[Unit] = ref
    .modify(previous => id -> println(s"$previous->$id"))
  // We perform a side effect (the println) as part of the return value of the function passed to modify
    .replicateA(3) // replicateA represents an effect n times
    .void
}
