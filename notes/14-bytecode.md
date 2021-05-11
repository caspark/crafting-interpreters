1. More memory efficient line number encoding

See branch `chap14-challenge-1`

2. CONSTANT_LONG opcode

See branch `chap14-challenge-2`

Downsides of this:

* "uses up" an opcode
* use of many constants bloats chunks, which might slow down execution in certain pathological scenarios.

3. Investigate allocators and re-implement one if you want

Some interesting reading on the topic for JEMalloc:

* https://medium.com/iskakaushik/eli5-jemalloc-e9bd412abd70
* https://www.facebook.com/notes/facebook-engineering/scalable-memory-allocation-using-jemalloc/480222803919
* https://stackoverflow.com/a/1624744/775982

But, out of time to try the re-implementation.
