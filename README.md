# Crafting Interpreters

Implementation of interpreters from [Crafting Interpreters](http://craftinginterpreters.com).

# Jlox

## Running

### Convenience script

Build and start:

```shell
./jlox.sh # interactive
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

# Clox

## Running

### Convenience script

Build and start clox, assuming you have cmake and make:

```shell
./clox.sh
```
