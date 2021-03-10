> Write some sample Lox programs and run them (you can use the implementations of Lox in my repository). Try to come up with edge case behavior I didn’t specify here. Does it do what you expect? Why or why not?


// aborts - makes it clear error handling isn't implemented
class World {
    init(happy, thing) {
        var x = "a" + "b";
        var y = 0;
        print(x * y);
    }
}

World(0, 1);

// function passing
fun hello() {
    fun dog() {
        return 4;
    }
    return dog;
}

print(hello()());

// anonymous function doesn't work :(
// fun bye() {
//     return fun() {
//         return 2;
//     }
// }



> This informal introduction leaves a lot unspecified. List several open questions you have about the language’s syntax and semantics. What do you think the answers should be?

* exception/error handling behavior
* numbers - are they strict IEEE floating point numbers? (probably not since it's JVM and they're opt in on the JVM)
* strings - encoding & handling of invalid encodings? byte vs codepoint vs grapheme unicode things? (UTF-8, separate functions for each)
* garbage collection guarantees - finalizer hooks, behavior, cycle handling, etc


> Lox is a pretty tiny language. What features do you think it is missing that would make it annoying to use for real programs? (Aside from the standard library, of course.)

* concurrency/parallelism/memory model - well, one could write a book on this topic.
* strings - escape sequences, interpolation, raw strings? (arguably if they're not specified, they should exist)
* any kind of module/package/dependencies system
