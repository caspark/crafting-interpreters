1) What token types would you define to implement a scanner for string interpolation? What sequence of tokens would you emit for the above string literal? What tokens would you emit for: `"Nested ${"interpolation?! Are you ${"mad?!"}"}"`? Consider looking at other language implementations that support interpolation to see how they handle it.

I'd try having these new tokens:

```
TOKEN_QUOTE for "
TOKEN_DOLLAR for $
TOKEN_STRING_PART for the text that's inside the quotes but outside the `${}`
```

and reuse `TOKEN_LEFT_BRACE`/`TOKEN_RIGHT_BRACE` (and remove `TOKEN_STRING`, since you can't tell `TOKEN_STRING` apart from a string that requires interpolation unless you implement backing up).

`"${drink} will be ready in ${steep + cool} minutes."` would be:

```
TOKEN_QUOTE
TOKEN_DOLLAR
TOKEN_LEFT_BRACE
TOKEN_IDENTIFIER - `drink`
TOKEN_RIGHT_BRACE
TOKEN_STRING_PART - ` will be ready in `
TOKEN_DOLLAR
TOKEN_LEFT_BRACE
TOKEN_IDENTIFIER - `steep`
TOKEN_PLUS
TOKEN_IDENTIFIER - `cool`
TOKEN_RIGHT_BRACE
TOKEN_STRING_PART - ` minutes.`
TOKEN_QUOTE
```

`"Nested ${"interpolation?! Are you ${"mad?!"}"}"` would be:

```
TOKEN_QUOTE
TOKEN_STRING_PART - `Nested `
TOKEN_DOLLAR
TOKEN_LEFT_BRACE
TOKEN_QUOTE
TOKEN_STRING_PART - `interpolation?! Are you `
TOKEN_DOLLAR
TOKEN_LEFT_BRACE
TOKEN_QUOTE
TOKEN_STRING_PART - `mad?!`
TOKEN_QUOTE
TOKEN_RIGHT_BRACE
TOKEN_QUOTE
TOKEN_RIGHT_BRACE
TOKEN_QUOTE
```

Re how other languages handle it, I looked at Ruby, which was quite convoluted:

* String parsing via `parse_string` is at https://github.com/ruby/ruby/blob/master/parse.y#L7335
    * All the annoying %-special-strings are parsed at at `parse_percent` at https://github.com/ruby/ruby/blob/master/parse.y#L8710
* This calls handling string contents via `tokadd_string` at https://github.com/ruby/ruby/blob/master/parse.y#L7065
    * Near as I can tell, state for what string type is being parsed is stored at `lex.strterm` in parser struct
    * String behavior is bucketed into one of the supported types via `enum string_type` - if that has the `STR_FUNC_EXPAND` bit set to 1, then variable expansion is allowed
    * The actual handling for the `#{}` syntax is at https://github.com/ruby/ruby/blob/master/parse.y#L7376
        * That calls parser_peek_variable_name which emits tSTRING_DBEG if it's a `#{}`
    * Handling the tSTRING_DBEG token is done at https://github.com/ruby/ruby/blob/master/parse.y#4884 in the YACC rules
        * The "is parsing string state" setting is turned off temporarily, then a `compstmt` is parsed (top level ruby expression) followed by a `tSTRING_DEND` (`}`), then we restore the state for parsing the string again.
    * Eventually, a string is created via the `STR_NEW3` macro which invokes `parser_str_new` over in `parse_string`

After spending way too much time puzzling much of this out by reading the code, I stumbled on https://ruby-hacking-guide.github.io/parser.html , which is quite old but helped explain some things I wasn't sure of, like how the YACC rules work. String section is at the bottom.

To conclude, there's this gem from the end of the String section:

>  As I previously described, there are a lot of conditions, it is tremendously being a spaghetti code.

(and it has only gotten worse since the guide was written - more state, lots of `goto` usage within functions)

2) How do Java and C# avoid (early versions of) C++'s original `vector<vector<string>>` by specifying and implementing it differently?

[Ran out of time]

3) Name a few contextual keywords from other languages, and the context where they are meaningful. What are the pros and cons of having contextual keywords? How would you implement them in your language’s front end if you needed to?

[Ran out of time]
