# Lox Grammar

A file to keep track of Lox's grammar, given that various challenges have caused some extensions to be added to it.

```
program        -> declaration* EOF ;

declaration    -> classDecl | funDecl | varDecl | statement ;

classDecl      -> "class" IDENTIFIER ( "<" IDENTIFIER )? "{" function* "}" ;

funDecl        -> "fun" function ;
function       -> IDENTIFIER "(" parameters? ")" block ;
parameters     -> IDENTIFIER ( "," IDENTIFIER )* ;

varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;

statement      -> exprStmt | forStmt | ifStmt | printStmt | returnStmt | whileStmt | breakSmt | block ;

exprStmt       -> expression ";" ;
forStmt        -> "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement; 
ifStmt         -> "if" "(" expression ")" statement ( "else" statement )? ;
printStmt      -> "print" expression ";" ;
returnStmt     -> "return" expression? ";" ;
whileStmt      -> "while" "(" expression ")" statement ;
breakStmt      -> "break" ;
block          -> "{" declaration* "}" ;

expression     -> comma
comma          -> funExp ( "," funExp)* ;
funExp         -> "fun" "(" parameters? ")" block | assignment ;
assignment     -> ( call "." )? IDENTIFIER "=" assignment | conditional ;
conditional    -> logic_or ("?" expression ":" conditional )? ;
logic_or       -> logic_and ( "or" logic_and )* ;
logic_and      -> equality ( "and" equality )* ;
equality       -> ( "!=" | "==" ) | ( comparison ( ( "!=" | "==" ) comparison)* ) ; // <-- changed
comparison     -> term ( ( ">" | ">=" | "<" | "<=" ) term)* ;
term           -> factor ( ( "-" | "+" ) factor)* ;
factor         -> unary ( ( "/" | "*" ) unary )* ;
unary          -> ( "!" | "-" ) unary | call;
call           -> primary ( "(" arguments? ")" | "." IDENTIFIER )* ;
primary        -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER | "super" "." IDENTIFIER ;

# arguments is composed of `assignment` rather than `expression` to avoid comma operator being parsed
arguments      -> assignment ( "," assignment )* ;
```
