```scala
def racePair[A, B](lh: IO[A], rh: IO[B])(implicit cs: ContextShift[IO]):
  IO[Either[(A, Fiber[IO, B]), (Fiber[IO, A], B)]]
```

receive the “winning” value along with the Fiber of the race “loser”, so you can decide what you want to do with it.
If either of the effects has an error, the other is cancelled

We can complete our implementation of cancellation on error for
`myParMapN`

```scala
def myParMapN[A, B, C](ia: IO[A], ib: IO[B])(f: (A, B) => C): IO[C] = IO.racePair(ia, ib).flatMap { 
  case Left((a, fb)) => (IO.pure(a), fb.join).mapN(f) 
  case Right((fa, b)) => (fa.join, IO.pure(b)).mapN(f) 
}
```

If no errors occur, we’ll detect which finishes first,
and then join the other until completion,
and finally combine the values with our function f.

- [x] start both the `ia` and `ib` computations so they run concurrently (**fork** them)
- [x] wait for each result
- [x] cancel the **other** effect if `ia` or `ib` fails
- [x] finally combine the results with the f function

1. Concurrency allows us to control running computations.
   

2. A Fiber is our handle to this control. After we start a concurrent computation, we can cancel or join it (wait for completion).

   
3. Concurrently executing effects can be cancelled. 
   Cancelled effects are expected to stop executing via implicit or explicit cancelation boundaries.
   

4. We can race two computations to know who finished first. Higher-order effects like timeouts can be constructed using races
