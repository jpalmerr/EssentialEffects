# The parallel typeclass

We've seen, unlike `Future`, `IO`  *doesn't* provide any support for parallelism.
So how can we achieve it?
=> we need a new type that does

```scala
class Par[+A] { ... } 
object Par {
  def apply[A](ioa: IO[A]): Par[A] = ???
  def unwrap[A](pa: Par[A]): IO[A] = ??? 
}
```

- It won't have a `Monad` instance
    - makes sense since we don't want to do things sequentially
    
- It will have an `Applicative` instance
    - to compose independent values
    
```scala
implict def ap(implicit cs: ContextShift[IO]: Applocative[IO.Par]) = 
  new Applicative[IO.Par] {
    def pure[A](a: A): IO.Par[A] = IO.Par(a)
    def map[A, B](pa: IO.Par[A])(f: A => B): IO.Par[B] = ???
    def product[A, B](pa: IO.Par[A], pb: IO.Par[B]): IO.Par[(A, B)] = ???
  }
```

- We require a `ContextShift[IO]` to be able to switch computations to different threads.

- The implementation of product will ensure that pa and pb execute on different threads, using `cs`