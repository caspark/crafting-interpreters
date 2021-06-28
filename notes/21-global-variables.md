1. Optimize compiler so it doesn't store the same constant more than once. How does this affect your compiler vs VM performance? Is this the right tradeoff?

Optimized in `chap21-challenge-1`; relevant snippet is this:

```diff
 int addConstant(Chunk* chunk, Value value) {
+  for (uint8_t i = 0; i < chunk->constants.count; i++) {
+    if (valuesEqual(value, chunk->constants.values[i])) {
+      return i;
+    }
+  }
+
   writeValueArray(&chunk->constants, value);
   return chunk->constants.count - 1;
 }
```

This makes the compiler quite a bit slower due to constant definition running in quadratic time
complexity now, but makes runtime a tiny bit faster.

Is this the right tradeoff? For a language with a 256-max constant pool, yes, probably! Being able
to write a larger set of programs seems more useful than being a tiny bit faster. If the constant
pool were larger, then it'd come down to whether improving memory usage & cache efficiency at the
cost of a slower start is worth it or not.

2. Can you come up with a more efficient way to store and access global variables than looking up
the global variable in a hash table, without changing the semantics?

When compiling, every time you see the use of a global variable, assign it an incrementing number,
and use a hash table to record what number is equivalent to that global variable.

Then, use the number to index into an array of `Value`, so that `OP_GET_GLOBAL`, `OP_SET_GLOBAL` and
`OP_DEFINE_GLOBAL` would all take a number as their argument instead of a string.

This would move the hashing to compile-time instead of runtime, which would be strictly faster in
theory if each line is executed at least once (and with loops, is probably a lot faster in practice
too).

3. Should we report accessing a variable which has never been defined as a compile error?

Yes! It's almost always a mistake.

How do other languages handle it? Well, it's actually covered in an earlier chapter for jlox; see
http://craftinginterpreters.com/statements-and-state.html#design-note .
