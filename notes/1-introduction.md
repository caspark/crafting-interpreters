> There are at least six domain-specific languages used in the little system I cobbled together to write and publish this book. What are they?

Markdown, HTML, SASS (SCSS), Mustache, Make... .htaccess ?

> Get a “Hello, world!” program written and running in Java. Set up whatever makefiles or IDE projects you need to get it working. If you have a debugger, get comfortable with it and step through your program as it runs.

```bash
#!/usr/bin/env bash

cat <<EOF > Main.java
class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
EOF
javac Main.java && exec java Main $@
```

> Do the same thing for C. To get some practice with pointers, define a doubly linked list of heap-allocated strings. Write functions to insert, find, and delete items from it. Test them.

see 1-c/ dir
