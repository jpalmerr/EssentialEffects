import cats.effect.IO

/*
How can we integrate with any possible asynchronous interface?
For that we’re going to use the IO.async method to construct an IO value from a callback-based API
 */

def async[A](k: (Either[Throwable, A] => Unit) => Unit): IO[A]

type Callback[A] = Either[Throwable, A] => Unit

def readableAsync[A](k: CallBack[A] => Unit): IO[A]