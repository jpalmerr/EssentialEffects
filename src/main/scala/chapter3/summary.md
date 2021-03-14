# Summary

1. `IO` does not support parallel operations itself because it is a Monad.

2. The Parallel typeclass specifies the translation between a pair of effect types:
   one that is a Monad and the other that is “only” an Applicative
   
3. Parallel IO composition requires the ability to shift computations to other threads within the current ExecutionContext.

4. `parMapN`, `parTraverse`, `parSequence` are the parallel versions of
   (the sequential) `mapN`, `traverse`, and `sequence`.
   Errors are managed in a fail-fast manner.
