# Construction IO values

```scala
def delay[A](a: => A): IO[A]
```

IO.delay takes a call-by-name (lazily-evaluated) argument, delaying the evaluation
of the code

`IO.apply` is an alias for `IO.delay`, hence we can just wrap with `IO`

#### What if the side effect throws an exception?

```scala
val ohNoes: IO[Int] =  IO.delay(throw new RuntimeException("oh noes!"))
```

The throw side effect is delayed until the IO is executed, and then and only then it will throw the exception.

We can also construct IO values from existing “pure” values

```scala
val twelve: IO[Int] = IO.pure(12)
```

**Do not perform any side effects when calling `IO.pure`**,
because they will be eagerly evaluated and that will break substitution.

If you aren’t sure, use `IO.delay` or `IO.apply` to be safe.

We can also *lift* an exception into `IO`, as long as we provide the “expected” type had it succeeded

```scala
val ohNoes: IO[Int] = IO.raiseError(new RuntimeException("oh noes!"))
```

`Future` has its own transform method

```scala
def futurish: Future[String] = ???
val fut: IO[String] = IO.fromFuture(IO(futurish))
```

Notice we have to wrap the future in an IO still.

- a Future is scheduled for execution during its construction
- However, an IO effect must delay its execution
- so we delay the Future by wrapping its creation within its own IO value





