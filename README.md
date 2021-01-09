# Crafting Interpreters

Implementation of interpreters from [Crafting Interpreters](http://craftinginterpreters.com).

## Running

Start the interpreter:

```shell
gradle run
```

Interpret some code from a file:

```shell
gradle run foo.lox
```

## Developing

Run the AST generation tool:

```shell
gradle run generateAst
# you should see changes to Expr.java 
```