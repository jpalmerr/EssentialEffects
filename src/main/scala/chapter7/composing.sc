
import cats.effect._
import cats.implicits._

def setup[A, B, C, D] = {


  // Resource is a functor; we can map over it
  val resA: Resource[IO, A] = ???
  val resB: Resource[IO, B] = resA.map(makeB)

  def makeB(a: A): B = ???

  // Resource is an applicative; we can mapN over two or more values

  val resC: Resource[IO, C] = ???
  val resD: Resource[IO, D] =
    (resB, resC).mapN(makeD)

  def makeD(b: B, c: C): D = ???

  // Resource is a monad; we can flatMap over it


  val resultC: Resource[IO, C] = for {
    a <- resA
    c <- makeC(a) } yield c 1
  def makeC(a: A): Resource[IO, C] = ???
}
