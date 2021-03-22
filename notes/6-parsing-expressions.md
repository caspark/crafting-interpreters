> Q1: In C, a block is a statement form that allows you to pack a series of statements where a single one is expected. The comma operator is an analogous syntax for expressions. A comma-separated series of expressions can be given where a single expression is expected (except inside a function call’s argument list). At runtime, the comma operator evaluates the left operand and discards the result. Then it evaluates and returns the right operand.
>
> Add support for comma expressions. Give them the same precedence and associativity as in C. Write the grammar, and then implement the necessary parsing code.

Original grammar before stratification for precedence:

```
expression     -> literal
                  | unary
                  | binary
                  | grouping ;
literal        -> NUMBER | STRING | "true" | "false" | "nil" ;
grouping       -> "(" expression ")" ;
unary          -> ( "-" | "!" ) expression ;
binary         -> expression operator expression ;
operator       -> "==" | "!=" | "<" | "<=" | ">" | ">="
                  | "+"  | "-"  | "*" | "/" ;
```

Stratified for precedence, as of end of Chapter 6:

```
expression     -> equality
equality       -> comparison ( ( "!=" | "==" ) comparison)* ;
comparison     -> term ( ( ">" | ">=" | "<" | "<=" ) term)* ;
term           -> factor ( ( "-" | "+" ) factor)* ;
factor         -> unary ( ( "/" | "*" ) unary )* ;
unary          -> ( "!" | "-" ) unary | primary;
primary        -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
```

With comma statement support:

```
expression     -> comma
comma          -> equality ( "," equality)* ;
equality       -> comparison ( ( "!=" | "==" ) comparison)* ;
comparison     -> term ( ( ">" | ">=" | "<" | "<=" ) term)* ;
term           -> factor ( ( "-" | "+" ) factor)* ;
factor         -> unary ( ( "/" | "*" ) unary )* ;
unary          -> ( "!" | "-" ) unary | primary;
primary        -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
```

See newly added #comma() in Parser for code.

> Likewise, add support for the C-style conditional or “ternary” operator ?:. What precedence level is allowed between the ? and :? Is the whole operator left-associative or right-associative?


> Add error productions to handle each binary operator appearing without a left-hand operand. In other words, detect a binary operator appearing at the beginning of an expression. Report that as an error, but also parse and discard a right-hand operand with the appropriate precedence.
