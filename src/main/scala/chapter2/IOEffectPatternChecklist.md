# Checklist

1a. 

An IO represents a (possibly) side effecting computation.

 b.
 
A value of type `A`, if the computation is successful.
 
2.

Externally-visible side effects are required: when executed, an IO can do anything, including side effects

We describe IO values by various constructors,
and describe compound effects by composing them with methods like
`map`, `mapN`, `flatMap`, and so on.
The execution of the effect only happens when an `unsafe*` method is called.

=> Therefore, IO is an effect!