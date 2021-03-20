# Shifting Contexts

Parallelism makes use of a set of resources to execute effects
On the JVM, this is a thread pool

Scala’s main abstraction for using thread pools is the scala.concurrent.ExecutionContext,
and Cats Effect builds on top of it to implement parallelism and concurrency.

