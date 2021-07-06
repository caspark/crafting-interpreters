1. Come up with a faster way of resolving local variables than scanning through the array of local variables.

A faster way would be using a hash table: you keep a hash table which records which Token resolves to which slot in the array.

But, then you have to make a hash table for it and you have to remove the entries from the hash table too.

Is the additional complexity worth it? Per "Bob's rule" of (anything quadratic will be a problem in a compiler sooner or later), yeah, probably it would be for a production compiler.

2. How do other languages handle code like `var a = a;`, and what (& why) would you do if it was your language?

Other languages:

* Python: complain that `a` isn't initialized
* JS: with `var`, variable hoisting means that `a` happily evaluates to undefined. With `let`, you get an error like in lox.
* Ruby: happily treats the `a` variable as `nil` initially
* Rust: complains that `a` is not found in scope

What would I do? Split variable declaration and definition like in lox.

Why? Catches errors earlier.

3. Add const (as in JS, not C) local variables to lox, so that assigning to a const variable causes a compile error.

Implemented in `chap22-challenge3`, though note I only implemented local immutable variables and punted on implementing the same for global variables.

I chose `let` syntax because it is the same number of characters as `var`, but it's more visually distinct than `val`.

4. Extend clox to allow more than 256 local variables to be in scope at a time.

Ran out of time.
