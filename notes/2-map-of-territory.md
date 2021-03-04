> Pick an open source implementation of a language you like. Download the source code and poke around in it. Try to find the code that implements the scanner and parser. Are they handwritten, or generated using tools like Lex and Yacc? (.l or .y files usually imply the latter.)

Ruby - generated, which I know because we looked at this for `Ruby Under a Microscope` ;)

> Just-in-time compilation tends to be the fastest way to implement dynamically typed languages, but not all of them use it. What reasons are there to not JIT?

* Implementation complexity
* Magnifies security issues if done poorly (arbitrary code execution potentially)
* Inconsistent run-times for code, which can be undesirable in certain niches

> Most Lisp implementations that compile to C also contain an interpreter that lets them execute Lisp code on the fly as well. Why?

* Lisp code is easy to parse because its on disk representation matches up to its in-memory AST representation very closely. And once you have an in-memory AST of a lisp program, you can build an interpreter in a few hundred lines: take the first element of a list, use a lookup table to find the corresponding function, pass the rest of the list as input to the function.
* Also, a defining feature of Lisp implementations is that they allow defining new code dynamically at runtime - so I'd say you essentially have to build this anyway?
