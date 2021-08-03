1. `Obj` now has `type`, `isMarked`, and `next` fields - how much memory do they take up, can you come up with something more compact, and is there a cost to that?

There are 5 types of objects, so we need 5 bits of information for that.

Then we need an additional 1 bit for `isMarked`, so 6 bits plus a pointer.

That those 6 bits should be able to be packed into the `next` pointer using a https://en.wikipedia.org/wiki/Tagged_pointer - e.g. https://github.com/HDembinski/stateful_pointer is for C++ but claims that it can pack another 24 bits of information into a pointer.

2. When the sweep phase traverses a live object, it clears the `isMarked` field to prepare it for the next collection cycle. Can you come up with a more efficient approach?

Use flip-flopping approach to vary behavior between 1, 3, 5, etc and 2, 4, 6 etc runs:

* Leave `isMarked` set to `true` after finishing each "odd" GC.
* Every "even" GC, instead set `isMarked` set to `false` for objects which are still referenced. In this case, all `isMarked` objects set to `true` should be freed, since they must have become unreachable since the "odd" GC.

3. Mark-sweep is only one of a variety of garbage collection algorithms out there. Explore those by replacing or augmenting the current collector with another one. Good candidates to consider are reference counting, Cheney’s algorithm, or the Lisp 2 mark-compact algorithm.

Out of time.
