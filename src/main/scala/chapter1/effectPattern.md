# The Effect Pattern

If we impose some conditions,
we can tame the side effects into something safer;
we’ll call these effects. There are two parts:

**1. The type of the program should tell us what kind of effects the program will perform,
in addition to the type of the value it will produce**

One problem with impure code is we can’t see that it is impure!
From the outside it looks like a method or block of code.
By giving the effect a type we can distinguish it from other code.

**2. If the behavior we want relies upon some externally-visible side effect,
we separate describing the effects we want to happen from actually making them happen.
We can freely substitute the description of effects up until the point we run them.**

We delay the side effect so it executes outside of any evaluation

*We’ll call these conditions the Effect Pattern*

## Effect Pattern Checklist

1. Does the program tell us 
    - what kind of effects the program will perform
    - what type of value will it produce?
    
2. When externally-visible side effects are required,
   is the effect description separate from the execution?
   
## Is `option` an effect?

*Does Option[A] tell us what kind of effects the program will perform,
in addition to the type of the value it will produce?*

- **Yes**:
  if we have a value of type `Option[A]`,
  we know the effect is optionality from the name Option,
  and we know it may produce a value of type A from the type parameter A.
  
*Are externally-visible side effects required?*

- **Not really**:

The Option data type is an interface representing optionality that maintains substitution.
We can replace a method call with its implementation and the meaning of the program won’t change.

One exception where an externally visible side effect might occur:

```scala
def get(): A 
```

Calling get on a None is a programmer error, and raises an exception

=> we can say yes, `Option[A]` is an effect!

#### Checklist

1. 
    
- *what kind of effects the program will perform?*
      
The Option type represents optionality

- *what type of value will it produce*?

A value of type A, if one exists

2. *is the effect description separate from the execution?*

No 

=> Therefore, Option is an effect

## Future

Future does not separate effect description from execution: it is unsafe.

## Capturing arbitrary side effects as an effect

What about an effect that does involve side effects, but safely?

This is the purpose of the IO effect type in cats.effect. It is a data type that allows us to capture any side effect,
but in a safe way, following our Effect Pattern.
