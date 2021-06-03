1. We could reduce our binary operators even further than we did here. Which other instructions can you eliminate, and how would the compiler cope with their absence?

What binary operators can we eliminate?

```c
OP_EQUAL,
OP_GREATER, // we could eliminate GREATER by using EQUAL and LESS (or the opposite)
OP_LESS,
OP_ADD,
OP_SUBTRACT, // we already covered we can replace this with ADD and NEGATE
OP_MULTIPLY, // in future, we could replace MULTIPLY with ADD and a loop
OP_DIVIDE,
```

2. Conversely, we can improve the speed of our bytecode VM by adding more specific instructions that correspond to higher-level operations. What instructions would you define to speed up the kind of user code we added support for in this chapter?

Obviously add GREATER_OR_EQUAL and LESS_OR_EQUAL.

Could also add EQUAL_NIL, EQUAL_ZERO, EQUAL_ONE since those would be fairly common operations in Lox.
