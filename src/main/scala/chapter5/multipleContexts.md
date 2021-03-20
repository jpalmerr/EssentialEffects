# The need for multiple contexts

- We can have many threads running
- we can pool those threads into logical groups with data types like ExecutionContext
- all fine if we are computing with pure values


- if we start interacting with the external environment
    - reading from a file, writing to the network
- our threads can become **blocked**

We’ll call the former kind of work `CPU-bound`
and the latter the blocking kind `I/O-bound”`.

(I/O refers to input/output, cats.effect.IO.)

- When a thread is blocked 
    - the JVM suspends its execution so another thread can be executed
- at the same time
    - there can be limits to the number of possible threads
    - usually part of when we configure out thread pools

**What do we do if our pool has at most n threads, but all those threads are blocked?**

To ensure work proceeds when I/O work is blocked:

-  isolate the CPU-bound work from any I/O-bound tasks by having separate pools
- cats effects encourages separate contexts
- CPU-bound work will be scheduled on a fixed-size thread pool
- I/O-bound work will be scheduled on an unbounded thread pool
    - blocked threads merely take up memory instead of stopping the progress of other tasks

In an IOApp on the JVM the default ExecutionContext is configured for CPU-bound work