# Concurrent Control

So far we can describe and run effects.

We don't yet have any way to control a *running* computation.

Because a computation may be running, to control it means we will be acting *concurrently* with it.

How to

- fork and join a concurrent effect
- cancel a concurrently running effect
- race mutliple effects 