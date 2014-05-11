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

The compiler requires Java 7 and is built using Apache Ant.

Type `ant` in the top-level directory. The default ANT target builds the
compiler, runs the unit tests, and produces the compiler JAR file (`mjc.jar`).
See `ant -projecthelp` for other available targets.


Running
-------
The compiler is invoked using the `mjc` script in the top-level directory.
The available command line options are:

    usage: mjc <infile> [options]
     -S             only output Jasmin assembly code
     -p             print abstract syntax tree
     -g             print abstract syntax tree in GraphViz format
     -h             show help message

For example, type `./mjc foo.java` to compile `foo.java`. The result is
written as a set of Jasmin assembly code files (`.j`), one file for each
class, along with corresponding `.class` files from running the Jasmin
assembler.

