1. Implement a faster approach for calling object constructors (`init`) and benchmark it

TODO

2. Most calls are actually not polymorphic even if the language says they can be - how do advanced languages optimize that?

Total guess: some sort of memoization? E.g. the JIT compiler remembers what was the last method that a given call site called and caches it for the call site along with the class name?

3. Fields shadowing methods means 2 hashtable lookups for a method call. That design reduces performance - was that the right call? What would you do in your language?

On the one hand, it might be the right call, if field access was more common than method access - depends on what programs people write.

On the other hand, no, I don't think it is the right call for my taste: I would allow only white listed fields to be set on a given object instance. That would allow the compiler to check at compilation time that no methods overlap with fields, which means that if you see something that looks like a call, you can check in the methods table first (where it is most likely to be) before checking the fields.

Cons:
* It's less flexible because you can't treat a field as

Pros:
* Faster at runtime due to 1 fewer hashtable lookup most of the time.
* As a reader, if there's one place that all attributes and methods are listed, it makes code more predictable for the reader.
