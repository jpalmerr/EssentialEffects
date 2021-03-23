import cats.effect.{ContextShift, IO}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

// lets mimic IO.fromFuture using async

trait Api {
  def compute: Future[Int] = ???
}

def doSomething[A](api: Api)(implicit ec: ExecutionContext, cs: ContextShift[IO]): IO[Int] = {
  IO.async[Int] { callBack =>
    api.compute.onComplete {
      case Failure(t) => callBack(Left(t))
      case Success(a) => callBack(Right(a))
    }
  }.guarantee(IO.shift)
}