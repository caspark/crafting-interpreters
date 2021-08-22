1. How would you address ensuring that fields don't get clobbered, if Lox was your language? Implement that.

I would require valid fields to be declared upfront for a class, so that the compiler can statically guarantee that subclasses do not overlap with fields.

Something like this:
```lox
class Barista {
    this: name;
}

class CoffeeMaker < Barista {
  this: coffee, audience;
  super: name;

  init(coffee, audience) {
    this.coffee = coffee;
    this.audience = audience;
    this.name = "CoffeeMaster 5000";
  }

  brew() {
    print "Enjoy your cup of " + this.coffee + " my good " + this.audience + " made by " + this.name;
    this.brewed = false; // would throw an error: field is not declared upfront
  }
}
```

```c
  // in compiler.c's classDeclaration()

    namedVariable(className, false);
  consume(TOKEN_LEFT_BRACE, "Expect '{' before class body.");
  while (!check(TOKEN_RIGHT_BRACE) && !check(TOKEN_EOF)) {
    if (match(TOKEN_THIS)) {
      consume(TOKEN_COLON, "Expect ':' after 'this' for field definition.");

      bool first = true;
      while (!check(TOKEN_SEMICOLON) && !check(TOKEN_EOF)) {
        if (!first) {
          consume(TOKEN_COMMA, "Expect ',' to separate this field definitions.");
        }
        consume(TOKEN_IDENTIFIER, "Expect field name for this.");
        first = false;
        printf("Found this field definition for '%.*s'\n", parser.previous.length, parser.previous.start);
        //TODO actually use token identifier
      }
      consume(TOKEN_SEMICOLON, "Expect ; to conclude finish this field definition");
    } else if (match(TOKEN_SUPER)) {
      consume(TOKEN_COLON, "Expect ':' after 'super' for field definition.");

      bool first = true;
      while (!check(TOKEN_SEMICOLON) && !check(TOKEN_EOF)) {
        if (!first) {
          consume(TOKEN_COMMA, "Expect ',' to separate super field definitions.");
        }
        consume(TOKEN_IDENTIFIER, "Expect field name for super.");
        first = false;
        printf("Found super field definition for '%.*s'\n", parser.previous.length, parser.previous.start);
        //TODO actually use token identifier
      }
      consume(TOKEN_SEMICOLON, "Expect ; to conclude super finish field definition");
    } else {
      method();
    }
  }
  consume(TOKEN_RIGHT_BRACE, "Expect '}' after class body.");
  emitByte(OP_POP);
```

However actually implementing that is very painful right now, because the compiler intentionally forgets about a class after it has compiled it. We'd have to maintain a table of class names to used field names, so that we can error when we see the same field get declared again (or when a method overlaps with it: once we have the information at compile time we may as well use it to speed up method calls too).

2. How do languages which support monkey-patching (e.g. Ruby) improve super call performance without the ability to use the "copy down" optimization trick?

According to `Ruby under a Microscope`, pages 142-144 (`Method Lookup and Constant Lookup`, sections `The Global Method Cache` and `The Inline Method Cache` and `clearing Ruby's Method Caches`):

* There's a global cache which maps the receiver class + method call to the resulting class's method
* There's an inline cache which records which resulting class & method a given method call hit last time
* The caches get cleared whenever a method is created or removed (or module is included into a class, etc)

3. Implement BETA's method calling approach, where superclass methods win out over subclass methods with the same name.

No time for this!
