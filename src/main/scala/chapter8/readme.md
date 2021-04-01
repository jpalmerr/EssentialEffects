# Testing effects

- use traits around methods to allow us to implement a "real" one and a "fake" one
    - technique I use regularly

- test execution context

```scala
import cats.effect.laws.util.TestContext
val ctx = TestContext()

implicit val cs: ContextShift[IO] = ctx.ioContextShift
implicit val timer: Timer[IO] = ctx.timer
```

For test context

```
"org.typelevel" %% "cats-effect-laws" % "2.1.3"
```

