MiniJava Compiler
=================

This is a compiler for a slightly modified version of the MiniJava
language described in Appel's *Modern Compiler Implementation in Java*,
created as part of the course DD2488 Compiler Construction at KTH.

Building / Running
------------------

Type `ant` in the top-level directory. This will build the compiler
and unit tests, and produce `xjc.jar` in the top-level directory.
To run the compiler, type

    java -cp xjc.jar:lib/* mjc.ARMMain foo.java -S

where `foo.java` is the MiniJava source file to be compiled.
