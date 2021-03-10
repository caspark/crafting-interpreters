> The lexical grammars of Python and Haskell are not regular. What does that mean, and why aren’t they?

They're not regular because they allow arbitrary nesting of whitespace. So you need to "remember" how deep you are indented in order to figure out what you're scanning, and regular grammars can't do that remembering.

> Aside from separating tokens—distinguishing print foo from printfoo—spaces aren’t used for much in most languages. However, in a couple of dark corners, a space does affect how code is parsed in CoffeeScript, Ruby, and the C preprocessor. Where and what effect does it have in each of those languages?

ruby - a space can affect postfix method invocation syntax - `puts(not true, false)` is ambiguous because it's unclear if `false` is an arg for `not` or `puts`
coffeescript - tabs instead of {}, spaces instead of `,` for array literals, also space between method name and parens for a method invocation can "add more" to the method invocation - https://stackoverflow.com/questions/16314251/coffeescript-issue-with-space
C - ??

> Our scanner here, like most, discards comments and whitespace since those aren’t needed by the parser. Why might you want to write a scanner that does not discard those? What would it be useful for?

When you want to build a parse tree. Useful for doing automated refactorings that preserve whitespace + comments, and other source to source transformations. Also useful for:

* jumping cursor to definition of a symbol
* accessibility - selecting all of a method
* syntax highlighting (though normally done via regexes)

> Add support to Lox’s scanner for C-style /* ... */ block comments. Make sure to handle newlines in them. Consider allowing them to nest. Is adding support for nesting more work than you expected? Why?

Done.
