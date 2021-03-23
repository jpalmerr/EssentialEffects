import cats.effect.{ContextShift, IO, IOApp}

import scala.concurrent.Future

implicit val ce: ContextShift[IO] = ???

def asFuture(): Future[String] = Future.successful("woo!")
val asIO: IO[String] = IO.fromFuture(IO(asFuture))