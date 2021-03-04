import cats.Parallel
import cats.effect.{IO, IOApp}
import cats.implicits._

// bit verbose to switch from par to seq, would look something like

object Abc extends IOApp {
  type A
  type B
  type C

  val ia: IO[A] = IO(???)
  val ib: IO[B] = IO(???)

  def f(a: A, b: B): C = ???

//  val ipa: IO.Par[A] = IO.Par(ia)
  val ipa: IO.Par[A] = Parallel[IO].parallel(ia)
//  val ipb: IO.Par[B] = IO.Par(ib)
  val ipb: IO.Par[B] = Parallel[IO].parallel(ib)
  val ipc: IO.Par[C] = (ipa, ipb).mapN(f)

//  val ic: IO[C] = IO.Par.unwrap(ipc)
  val ic1: IO[C] = Parallel[IO].sequential(ipc)

  // or even skip a step
  val ic2: IO[C] = (ia, ib).parMapN(f)

  override def run(args: List[String]) = ???
}

/*
See Cats library trait Parallel

captures concept of translating between two

def sequential: P ~> S
def parallel: S ~> P

can think of it as going from monad to applicative :)
 */
