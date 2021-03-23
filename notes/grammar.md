# Lox Grammar

A file to keep track of Lox's grammar, given that various challenges have caused some extensions to be added to it.

```
program        -> declaration* EOF ;

declaration    -> varDecl
                | statement ;

varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;

statement      -> exprStmt | printStmt | block;

exprStmt       -> expression ";" ;
printStmt      -> "print" expression ";" ;
block          -> "{" declaration* "}" ;

expression     -> comma
comma          -> assignment ( "," assignment)* ;
assignment     -> IDENTIFIER "=" assignment | conditional ;
conditional    -> equality ("?" expression ":" conditional )? ;
equality       -> ( "!=" | "==" ) | ( comparison ( ( "!=" | "==" ) comparison)* ) ; // <-- changed
comparison     -> term ( ( ">" | ">=" | "<" | "<=" ) term)* ;
term           -> factor ( ( "-" | "+" ) factor)* ;
factor         -> unary ( ( "/" | "*" ) unary )* ;
unary          -> ( "!" | "-" ) unary | primary;
primary        -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER ;
```