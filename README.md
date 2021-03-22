# Crafting Interpreters

Implementation of interpreters from [Crafting Interpreters](http://craftinginterpreters.com).

## Running

### Convenience script

Build and start Lox:

```shell
./jlox.sh # interactive
rlwrap ./jlox.sh # with readline support if rlwrap is installed
./jlox.sh app/foo.lox # batch
```

### Step by step

First build the code:

```shell
gradle installDist
```

Start the interpreter:

```shell
./app/build/install/app/bin/app
```

Interpret some code from a file:

```shell
./app/build/install/app/bin/app app/foo.lox
```

## Developing

Run the AST generation tool:

```shell
gradle run generateAst
# you should see changes to Expr.java 
```