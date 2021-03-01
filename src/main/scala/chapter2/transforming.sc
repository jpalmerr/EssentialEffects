import cats.effect.IO
import cats.implicits._

// Transforming IO's

// IO is a functor; we can map over it

IO(12).map(_ + 1).unsafeRunSync()

// IO is an applicative; we can mapN over two or more values

(IO(12), IO("hi")).mapN((i, s) => s"$s: $i").unsafeRunSync()

// IO is a monad; we can flatMap over it

(for {
  i <- IO(12)
  j <- IO(i + 1)
} yield j.toString).unsafeRunSync()

// Error Handling

/*
As we’ve seen, an IO computation can fail,
either by throwing an exception during execution,
or by capturing an existing exception via IO.raiseError.

We can, however, detect these failures and do something about it.

def handleErrorWith[AA >: A](f: Throwable => IO[AA]): IO[AA]
*/

val ohNoes = IO.raiseError[Int](new RuntimeException("oh noes!"))
val handled: IO[Int] = ohNoes.handleErrorWith(_ => IO(12))

// handleErrorWith doesn’t need to produce a successful effect

val handledWithError: IO[Int] = ohNoes.handleErrorWith(t => IO.raiseError(new Throwable(t)))

// If we explicitly want to transform the error into another error, we could use adaptError instead

ohNoes.adaptError(t => new Throwable(t))

// see MonadError

/*
There is an interesting alternative to this where
we instead handle errors by transforming them into Either values,
so an IO[A] now becomes an IO[Either[Throwable, A]]
*/

val attempted: IO[Either[Throwable, Int]] = ohNoes.attempt


