> Our interpreter carefully checks that the number of arguments passed to a function matches the number of parameters it expects. Since this check is done at runtime on every call, it has a performance cost. Smalltalk implementations don’t have that problem. Why not?

Not sure about this answer, however: 

It seems like Smalltalk messages have at most 1 argument, then if you send multiple messages together then all the messages are mashed together and used to look up the appropriate method implementation. So the arity check never happens because it is implicit in checking whether the message is responded to. 


> Lox’s function declaration syntax performs two independent operations. It creates a function and also binds it to a name. This improves usability for the common case where you do want to associate a name with the function. But in functional-styled code, you often want to create a function to immediately pass it to some other function or return it. In that case, it doesn’t need a name.
> 
> Languages that encourage a functional style usually support anonymous functions or lambdas—an expression syntax that creates a function without binding it to a name. Add anonymous function syntax to Lox so that this works:
>
> ```
> fun thrice(fn) {
>   for (var i = 1; i <= 3; i = i + 1) {
>     fn(i);
>   }
> }
> 
> thrice(fun (a) {
>   print a;
> });
> // "1".
> // "2".
> // "3".
> ```
> 
> How do you handle the tricky case of an anonymous function expression occurring in an expression statement, such as `fun () {};`?

Done, see LoxInlineFunction and friends.

Re anonymous function expression occurring in an expression statement - I didn't allow it :) Causes a parser error.

> Is this program valid?
>
> ```
> fun scope(a) {
>   var a = "local";
> }
> ```
>
> In other words, are a function’s parameters in the same scope as its local variables, or in an outer scope? What does Lox do? What about other languages you are familiar with? What do you think a language should do?

* They're in the same scope as local variables.
* Lox lets params be rebound.
* Most languages I'm familiar with allow the same, except for e.g. `const foo` in JS/TS.
* In some languages it makes sense to allow it (e.g. Rust) for same reason as variable rebinding normally. I think it should generally be avoided otherwise (allowing the reader to make more assumptions about the code -> easier to read code). However, I don't think it makes sense to ban at a language level because sometimes it can be convenient to allow easy "cleaning" of input data to a function without risking the uncleaned original being used (a language with linear types or that allows explicit dropping of bindings could prevent that too, but both are rare).