1. `Obj` now has `type`, `isMarked`, and `next` fields - how much memory do they take up, can you come up with something more compact, and is there a cost to that?

TODO

2. When the sweep phase traverses a live object, it clears the `isMarked` field to prepare it for the next collection cycle. Can you come up with a more efficient approach?

TODO

3. Mark-sweep is only one of a variety of garbage collection algorithms out there. Explore those by replacing or augmenting the current collector with another one. Good candidates to consider are reference counting, Cheney’s algorithm, or the Lisp 2 mark-compact algorithm.

TODO
