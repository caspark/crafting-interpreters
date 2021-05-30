More resources on Pratt parsing:

* Rust - https://matklad.github.io/2020/04/13/simple-but-powerful-pratt-parsing.html
* Bob's Java version - http://journal.stuffwithstuff.com/2011/03/19/pratt-parsers-expression-parsing-made-easy/
* Summary of several blog posts on it - https://www.oilshell.org/blog/2017/03/31.html

1) Trace `(-1 + 2) * 3 - -4` - functions called, order they are called, which calls which, and the arguments passed to them.

```c
expression()
    parsePrecedence(PREC_ASSIGNMENT)
        getRule(token_minus)
        grouping() // prefix rule
            expression()
                parsePrecedence(PREC_ASSIGNMENT)
                    getRule(token_minus)
                    unary() // prefix rule
                        parsePrecedence(PREC_UNARY)
                            getRule(prev, token_number) // number was 1
                            number() // prefix rule
                                // extracts the number and emits constant 1
                            getRule(current, token_plus)
                            // precedence of rule too low, bail out
                        // emit negate bytecode
                    getRule(current, token_plus)
                    getRule(prev, token_plus)
                    binary() // infix rule
                        getRule(TOKEN_PLUS)
                        parsePrecedence(PREC_FACTOR) // +1 was applied
                            getRule(prev, TOKEN_NUMBER)
                            number() // prefix
                                // extracts the number and emits constant 2
                            getRule(current, RIGHT_PAREN)
                        // emit Add bytecode
                    getRule(current, RIGHT_PAREN)
            consume(right paren)
            getRule(current, TOKEN_STAR)
            getRule(prev, TOKEN_STAR)
            binary()
                getRule(operatortype, TOKEN_STAR)
                parsePrecedence(PREC_UNARY)
                    getRule(prev, number)
                    number()
                        // emit constant 3
                    getRule(current, minus)
                // emit multiply
            getRule(current, TOKEN_MINUS)
            getRule(prev, TOKEN_MINUS)
            binary()
                getRule(operatortype, TOKEN_MINUS)
                parsePrecedence(PREC_FACTOR)
                    getRule(prev, minus)
                    unary()
                        parsePrecedence(PREC_UNARY)
                            getRule(prev, number)
                            number()
                                // emit constant 4
                            getRule(current, end of file)
                        // emit negate
                    getRule(current, end of file)
                // emit subtract
            getRule(current, end of file)
endCompiler()
```

2) In Lox, what tokens other than MINUS can be used in both prefix and infix positions? What about C or in another language of your choice?

I don't think there are any others in Lox?

In Ruby, `+` is the same as minus.

In C, `*` can be multiply or a pointer.

3) Add support for "mixfix" expressions like C's ternary conditional operator `?:` to the parser (no need to implement it on the compiler/bytecode generation side).

TODO
