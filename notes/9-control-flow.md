> A few chapters from now, when Lox supports first-class functions and dynamic dispatch, we technically won’t need branching statements built into the language. Show how conditional execution can be implemented in terms of those. Name a language that uses this technique for its control flow.

Define classes for True and False sharing a common interface: each have a method to call a function if the class is an instance of True and a method to call a function if the class is an instance of False.  

> Likewise, looping can be implemented using those same tools, provided our interpreter supports an important optimization. What is it, and why is it necessary? Name a language that uses this technique for iteration.

Tail recursion, necessary to avoid overflowing the stack; Haskell and Scala both support it (though Scala requires an annotation to make it work).

> Unlike Lox, most other C-style languages also support break and continue statements inside loops. Add support for break statements.

> The syntax is a break keyword followed by a semicolon. It should be a syntax error to have a break statement appear outside of any enclosing loop. At runtime, a break statement causes execution to jump to the end of the nearest enclosing loop and proceeds from there. Note that the break may be nested inside other blocks and if statements that also need to be exited.

See code - `break` statement added.