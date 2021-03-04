package chapter3

/**
 * We've seen Future supports parallelism by
 * scheduling work on multiple threads via execution context
 */

import cats.implicits._
import scala.concurrent._
import scala.concurrent.duration._

object Future1 extends App {
  implicit val ec = ExecutionContext.global

  val hello = Future(println(s"[${Thread.currentThread.getName}] Hello"))
  val world = Future(println(s"[${Thread.currentThread.getName}] World"))

  val hw1: Future[Unit] =
    for {
      _ <- hello
      _ <- world
    } yield ()

  Await.ready(hw1, 5.seconds)

  val hw2: Future[Unit] =
    (hello, world).mapN((_, _) => ())

  Await.ready(hw2, 5.seconds)

  /**
   * Is the effect of hw1 the same as the effect of hw2
   *
   * We see only one pair of Hello and World printed. Why?
   * Future eagerly schedules the action and caches the result
   *
   * This breaks rule #2 of our Effect PatterN;
   * the unsafe side effect is *NOT separately executed
   */
}

object Future2 extends App {
  implicit val ec = ExecutionContext.global

  def hello: Future[Unit] = Future(println(s"[${Thread.currentThread.getName}] Hello"))
  def world: Future[Unit] = Future(println(s"[${Thread.currentThread.getName}] World"))


  val hw1: Future[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()
  Await.ready(hw1, 5.seconds)

  val hw2: Future[Unit] =
    (hello, world).mapN((_, _) => ())

  Await.ready(hw2, 5.seconds)
}

/**
 * What about IO? Does using mapN vs. flatMap have a different effect, like Future does?
 */

import cats.effect._

object IOComposition extends App {
  val hello = IO(println(s"[${Thread.currentThread.getName}] Hello"))
  val world = IO(println(s"[${Thread.currentThread.getName}] World"))

  val hw1: IO[Unit] = for {
    _ <- hello
    _ <- world
  } yield ()

  val hw2: IO[Unit] =
    (hello, world).mapN((_, _) => ())
  hw1.unsafeRunSync()
  hw2.unsafeRunSync()
}

/*
[main] Hello
[main] World
[main] Hello
[main] World
 */

/**
 * IO doesn’t provide any support for the effect of parallelism!
 * And this is by design, because we want different effects to have different types, as per our Effect Pattern
 */