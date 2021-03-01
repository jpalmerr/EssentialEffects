package chapter1

case class MyIO1[A](unsafeRun: () => A)

/**
 * The side effect we want to delay is captured as the function unsafeRun.
 * Named unsafeRun because we want to let everyone know this function does not maintain substitution.
 */

object MyIO1 {
  def putStr(s: => String): MyIO1[Unit] =
    MyIO1(() => println(s))
}

object Printing1 extends App {
  val hello = MyIO1.putStr("hello!")
  // We describe the printing of "hello!" as a MyIO value. But it hasn’t been executed yet.

  hello.unsafeRun() // explicitly run the effect
}

/**
 * We can construct individual effects, and run them, but how do we combine them?
 * We may want to modify the output of an effect (via map),
 * or use the output of an effect to create a new effect (via flatMap).
 *
 * Let’s add these methods to our MyIO.
 * But be careful! Composing effects must not execute them.
 * We require composition to maintain substitution, so we may build effects out of other effects.
 */

case class MyIO[A](unsafeRun: () => A) {

  /**
   * The definition of map is straightforward: We create a new MyIO that must return a value of type B.
   * How can we get a B? We have the A ⇒ B function f, so where can we get an A value?
   * We use unsafeRun.
   */

  def map[B](f: A => B): MyIO[B] = MyIO(() => f(unsafeRun()))

  /**
   * We create a new MyIO that must return a B.
   * We call f with the output of unsafeRun, but this gives us a MyIO[B], not a B.
   * But if we invoke unsafeRun on that MyIO, it will produce the B value we need.
   *
   * It sequences two operations, where one happens before the other
   */
  def flatMap[B](f: A => MyIO[B]): MyIO[B] = MyIO(() => f(unsafeRun()).unsafeRun())
}
object MyIO {
  def putStr(s: => String): MyIO[Unit] =
    MyIO(() => println(s))
}

object Printing extends App {
  val hello = MyIO.putStr("hello!")
  val world = MyIO.putStr("world!")

  val helloWorld =  for {
    _ <- hello
    _ <- world
  } yield ()

  helloWorld.unsafeRun()
}

/**
 * Effect Pattern Checklist
 *
 * 1a. MyIO represents a (possibly) side effecting computation.
 * 1b. A value of type A, if the computation is successful.
 *
 * 2.
 *    Externally-visible side effects are required: when executed, a MyIO can do anything, including side effects.
 *
 *    Constructing values and by composing with map and flatMap.
 *    The execution of the effect only happens when unsafeRun is called.
 */

