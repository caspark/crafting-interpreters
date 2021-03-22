> Allowing comparisons on types other than numbers could be useful. The operators might have a reasonable interpretation for strings. Even comparisons among mixed types, like 3 < "pancake" could be handy to enable things like ordered collections of heterogeneous types. Or it could simply lead to bugs and confusion.
> 
> Would you extend Lox to support comparing other types? If so, which pairs of types do you allow and how do you define their ordering? Justify your choices and compare them to other languages.

I'd support Strings (lexicographical ordering).

Booleans also - sure, `false` < `true` (since we map `false` to `1`).

I wouldn't support comparisons between heterogeneous types - that way lies a whole bunch of hard to remember rules, as I documented over in https://stackoverflow.com/questions/14687876/how-do-the-javascript-relational-comparison-operators-coerce-types many years ago.

> Many languages define + such that if either operand is a string, the other is converted to a string and the results are then concatenated. For example, "scone" + 4 would yield scone4. Extend the code in visitBinaryExpr() to support that.

```java
if (left instanceof String || right instanceof String) {
    String leftS = left instanceof String ? (String) left : stringify(left);
    String rightS = right instanceof String ? (String) right : stringify(right);
    return leftS + rightS;
}
```

> What happens right now if you divide a number by zero? What do you think should happen? Justify your choice. How do other languages you know handle division by zero, and why do they make the choices they do?
> 
> Change the implementation in visitBinaryExpr() to detect and report a runtime error for this case.

* Infinity is returned
* Runtime exception is probably easier to deal with - in most practical cases, dividing by zero results from a logic error, so best to make it easy to detect this.
* Other languages:
    * Rust - panics the thread, because dividing by 0 is undefined in llvm, and Rust hates undefined behavior.
    * Ruby - runtime exception
    * Python - same
    * Lua - infinity
  
```java
if ((double) right == 0) {
    throw new RuntimeError(expr.operator, "Division by zero");
}
```