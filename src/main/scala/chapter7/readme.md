# Managing resources

- A **network connection** maintains a connection to a remote system over some socket networking abstraction.
  Allocating sockets may be expensive, in addition to the time needed to actually establish a (remote) connection.
  And those sockets need to be reclaimed when they aren’t needed anymore.

- A **database connection**, like a network connection, also needs to talk to a remote system,
  and will have similar costs like the previous example.
  It may also manage its own, additional resources, such as threads, involved in the connection protocol.

the `Resource` data type represents this acquire-use-release pattern to manage state

```
You can think of Resource as a tiny DSL (domain-specific language)
to describe resource management in terms of individual IO effects
and the use method then “compiles” those instructions into a single IO value.
```