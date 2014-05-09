MiniJava Compiler (`mjc`)
=========================

This is a compiler for a slightly modified version of the MiniJava
language described in Appel's *Modern Compiler Implementation in Java*.
It was created as part of the course *DD2488 Compiler Construction* at
KTH. The compiler supports the basic language described
[here](http://www.csc.kth.se/utbildning/kth/kurser/DD2488/komp14/project/grammar14v1b.pdf),
with the following extensions:

| Code                    | Description                                  |
| ----------------------- | -------------------------------------------- |
| **IWE**                 | 'if' statements with or without 'else'       |
| **NBD**                 | nested blocks with new variable declarations |
| **ABC**                 | array bounds checks                          |
| **CLE CGT CGE CEQ CNE** | comparison operators                         |
| **BDJ**                 | logical OR connective                        |

Building
--------

Type `ant` in the top-level directory. This will build the compiler,
run the unit tests, and produce `mjc.jar`. See `ant -projecthelp` for
the different available targets. Here's a hopefully up to date list:

    Main targets:
     clean    Delete all generated files
     compile  Compile all sources
     doc      Generate JavaDoc documentation in doc/
     jar      Build compiler JAR file
     report   Build report PDF file
     submit   Submit TAR archive to TIGRIS
     tar      Build compressed source-only TAR archive
     test     Run the unit tests
    Default target: jar

The compiler requires Java 7.

Running
-------
The compiler is invoked using the `mjc` script in the top-level directory.
The available command line are:

    usage: mjc <infile> [options]
     -S             output Jasmin assembly code
     -p             print abstract syntax tree
     -g             print abstract syntax tree in GraphViz format
     -s             print symbol table
     -h             show help message

For example, type `./mjc foo.java -S` to compile `foo.java`. The result is
written as a set of Jasmin assembly code files (`.j`) into the current
working directory, one file for each class.

