# Concurrent Coordination

By coordination, we mean the behavior of one effect should depend on another

e.g.:

- how can we share state between effects when that state might be concurrently updated
- how can we ensure one effect only proceeds once work is complete in another

`Ref` data type for sharing mutable state

`Deferred` data type can provide concurrent effect serialization without blocking any actual threads