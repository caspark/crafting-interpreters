1. Make hash tables support other primitive types as keys. Also, how do user classes make things harder?

`chap20-challenge1` has a hacked up solution.

User defined classes make it harder because you need to have a way for users to specify the hashing and equality-comparison methods - e.g. conventions for specific methods on classes that implement that.

2. Research hash tables in different open source systems, see what they do and figure out why.

Lua - have a hash part and an array part, to optimize storing entries by numbers.

From https://www.lua.org/doc/jucs05.pdf -

> When a table needs to grow, Lua recomputes the sizes for its hash part and
its array part. Either part may be empty. The computed size of the array part
is the largest n such that at least half the slots between 1 and n are in use
(to avoid wasting space with sparse arrays) and there is at least one used slot
between n/2+1 and n (to avoid a size n when n/2 would do).

Hash portion uses a "chained scatter table with Brent's variation":

> A main invariant of these tables is that if an element is not in its main position
(i.e., the original position given by its hash value), then the colliding element
is in its own main position. In other words, there are collisions only when two
elements have the same main position (i.e., the same hash values for that table
size). There are no secondary collisions. Because of that, the load factor of these
tables can be 100% without performance penalties.

What is Brent's variation actually? I read the method section of
https://maths-people.anu.edu.au/~brent/pd/rpb013.pdf (and http://www.minkhollow.ca/Courses/461/Notes/Hashing/HashBrentex1.html); it seems to boil down to linear quotient probing, plus:

* If you try to insert an element X but element Y is in X's "most preferred" position AND Y is not
  in _its_ most preferred positio
    * Then move Y into its next preferred position and put X in its most preferred position

So the core idea is you assume each element will be:

* accessed more frequently than it is inserted
* accessed about as frequently as every other element in the hash

And if that's true then theoretically it should let you have load factors close to 100% without
horrible performance.

This logic seems to be at https://www.lua.org/source/5.3/ltable.c.html#luaH_newkey

3. Benchmark this hash table and document why you chose the benchmarks you did.

Performance work has been descoped due to the upcoming OKR.
