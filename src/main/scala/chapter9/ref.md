```scala
def get(): IO[A]
```

atomically set the state

```scala
def set(value: A): IO[Unit]
def getAndSet(value: A): IO[A] // Sets the value to A and returns the previous value of the Ref
```

update the current state

```scala
def update(f: A => A): IO[Unit]

def getAndUpdate(f: A => A): IO[A] // Updates the value with the function f and returns the previous value
def updateAndGet(f: A => A): IO[A] // Updates the value with the function f and returns the new value

def modify[B](f: A => (A, B)): IO[B]
```



