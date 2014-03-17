Compiler Test Programs
======================

This directory and its subdirectories contain input programs for the various
compiler tests. The tests are organized as follows:

* **good** contains programs that should compile without problems, but which
  may exhibit runtime errors. These are used by `TypeCheckerGoodTest.java`.
* **bad/syntax** contains programs with syntax errors. These are used by
  `ParserBadTest.java`.
* **bad/symbol** contains programs with symbol errors. These are used by
  `SymbolTableBuilderBadTest.java`.
* **bad/type** contains programs with type errors. These are used by
  `TypeCheckerBadTest.java`.
* **execute** contains programs that should compile and run without problems,
  and which have expected output.

The tests in **shared** are the tests that we will submit as part of the
project examination.
