> 1. Earlier, I said that the |, *, and + forms we added to our grammar metasyntax were just syntactic sugar. Take this grammar:
> 
> expr → expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+
     | IDENTIFIER
     | NUMBER
> 
> Produce a grammar that matches the same language but does not use any of that notational sugar.
> 
> Bonus: What kind of expression does this bit of grammar encode?

```
expr -> expr nestedExpr | IDENTIFIER | NUMBER

nestedExpr -> exprInside maybeExpr
maybeExpr -> nestedExpr
maybeExpr -> ()

exprInside -> "(" expr continuedExpr ")"
exprInside -> "." IDENTIFIER

continuedExpr -> "," continuedExpr
continuedExpr -> ()
```

> 2. The Visitor pattern lets you emulate the functional style in an object-oriented language. Devise a complementary pattern for a functional language. It should let you bundle all of the operations on one type together and let you define new types easily.
> (SML or Haskell would be ideal for this exercise, but Scheme or another Lisp works as well.)

Traits/typeclasses.

Essentially if you have a function `F(A) -> X` and you want to allow F to be extendable to support converting B, C, etc to X, then first define a new function `T(a) -> X`, which has a contract of taking an `A` and transforming it to an `X`.

Then change F to accept T, e.g. `F(A, T) -> X`.

Now you can trivially make F support input types other than A by also passing in a new version of T; for example, to support `F(B) -> X`, define a function `U(B) -> X`, and call F like `F(B, U)` to get `X`.

The contract of "pass in a function that can convert its input type to X" could be written as `_ -> X`, which we can loosely call a typeclass.

Finally, instead of a single function this could be an associative array of `Typeclass -> (_ -> _)`, thereby providing support for arbitrary functions, which could be passed into any function.

Thus, we've designed a mechanism to bundle all operations on one type together and define new types easily.

Equality
ToString

fn equals()
fn toString()

Float
Ints

```
fn joinIfNotEqual<X>(xs: List<X>, xCapabilities: Map<String, X -> Any>) -> String {
    seen = {}
    result = ""
    for (x in xs) {
        if isIntInDataStructure(x, seen):
            result += ", " + f(x)
            seen.add(x)
    }
    return result
}

floatBuilder(..) -> (Float, Map<String, () -> Any>) {
     float foo = ...;
     floatCapabilities = {
         toString: {
              foo.
         }
         inDataStructureChecker: {
               foo.whatever
         }
     }
     
     (foo, floatCapabilities)
}  

floatToString(x: float) -> String;
intToString(x: Int) -> String;

isFloatInDataStructure(x: Int) -> bool;
isIntInDataStructure(x: Int) -> bool;

floatCapabilities = {
    toString: floatToString
    inDataStructureChecker: IsfloatInDataStructure
}

intCapabilities = {
    toString: intToString
    inDataStructureChecker: IsIntInDataStructure
}

joinIfNotEqual(xs, floatCapabilities)
joinIfNotEqual(xs, intCapabilities)

```


> In Reverse Polish Notation (RPN), the operands to an arithmetic operator are both placed before the operator, so 1 + 2 becomes 1 2 +. Evaluation proceeds from left to right. Numbers are pushed onto an implicit stack. An arithmetic operator pops the top two numbers, performs the operation, and pushes the result. Thus, this:
> 
> (1 + 2) * (4 - 3)
> 
> in RPN becomes:
> 1 2 + 4 3 - *
> 
> Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN, and returns the resulting string.

See code.