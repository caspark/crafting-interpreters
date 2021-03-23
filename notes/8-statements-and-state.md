> The REPL no longer supports entering a single expression and automatically printing its result value. That’s a drag. Add support to the REPL to let users type in both statements and expressions. If they enter a statement, execute it. If they enter an expression, evaluate it and display the result value.

Done - interesting part of the code is:

```java
    private Stmt expressionStatement() {
        Expr expr = expression();
        if (check(SEMICOLON)) {
            advance();
            return new Stmt.Expression(expr);
        } else if (printNakedExpression) {
            return new Stmt.Print(expr);
        } else {
            throw error(peek(), "Expect ';' after expression.");
        }
    }
```

> Maybe you want Lox to be a little more explicit about variable initialization. Instead of implicitly initializing variables to nil, make it a runtime error to access a variable that has not been initialized or assigned to, as in:

Relevant changes:

```java
class Environment {
    public static final Object UNINITIALIZED_VALUE = new Object();
    
    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            Object val = values.get(name.lexeme);
            if (val == UNINITIALIZED_VALUE) { // intentional reference equality
                throw new RuntimeError(name, "Uninitialized variable '" + name.lexeme + "'.");
            } else {
                return val;
            }
        }
        
        // etc etc
    }
}

class Interpreter {
    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer == null) {
            environment.define(stmt.name.lexeme, Environment.UNINITIALIZED_VALUE);
        } else {
            value = evaluate(stmt.initializer);
            environment.define(stmt.name.lexeme, value);
        }

        return null;
    }
}
```

> What does the following program do?

```lox
var a = 1;
{
    var a = a + 2;
    print a;
}
```

> What did you expect it to do? Is it what you think it should do? What does analogous code in other languages you are familiar with do? What do you think users will expect this to do?

* Prints 3
* Yep
* Same as Rust, which allows variable rebinding while referencing that same variable data. Other languages I'm familiar with don't allow variable name rebinding (compile time error - e.g. Java) or use "initialization is declaration" semantics (e.g. Python, Ruby).
* ...hopefully users will expect it to return 3 as well :)