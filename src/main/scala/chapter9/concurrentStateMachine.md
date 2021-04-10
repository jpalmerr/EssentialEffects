# Concurrent State Machine

With `Ref` we can ensure atomic updates of shared state

`Deferred` gives us the ability to serialize the execution of an effect with respect to
some newly-produced state.

Together we can build larger and more complex concurrent behaviors.

One technique, a *concurrent state machine*:

1. Define an interface whose methods return an effect

2. Implement the interface by building a state machine where:
   - State (`S`) atomically managed via a `Ref[IO, S]` value
   - each interface method is implemented by a state transition function affecting the `Ref`
   - any state-dependent blocking behaviour is controlled via `Deferred values`
