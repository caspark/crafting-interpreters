1. How do other languages handle accessing a field that does not exist? Pick an approach and implement it.

Generally they provide some sort of keyword (or built in method on objects) that returns true or false when checking whether a field exists in Lox. E.g. Python's `in`.

Implemented `in` keyword in `chap27-challenge-1`.

2. Add a way to access a field using a dynamically created field name - e.g. `myObj["someField"]` or similar.

Implemented in `chap27-challenge-2`.

3. How do other languages implement removing a field? Pick an approach and implement it.

They provide either a method on an object or a language keyword to delete a field. E.g. Python's `del` keyword.

Implemented a `del X from Y` statement in `chap27-challenge-3`.

4. How do sophisticated implementations of dynamic languages speed up accessing property names by name?

They generate fixed classes under the hood, then use those to replace a "property access by name" with more efficient load/store operations. E.g. V8, in increasing order of technical details:

* https://chromium.googlesource.com/external/github.com/v8/v8.wiki/+/60dc23b22b18adc6a8902bd9693e386a3748040a/Design-Elements.md#fast-property-access
* https://mathiasbynens.be/notes/shapes-ics#ics
* https://v8.dev/blog/fast-properties
* https://docs.google.com/document/d/1mEhMn7dbaJv68lTAvzJRCQpImQoO6NZa61qRimVeA-k/edit#heading=h.3vstp36siek8
