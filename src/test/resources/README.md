Compiler Test Programs
======================

This directory and its subdirectories contain input programs for the various
compiler tests. The tests are organized as follows:

* **good** contains programs that should compile without problems, but which
  may exhibit runtime errors.
* **bad/syntax** contains programs with syntax errors.
* **bad/symbol** contains programs with symbol errors.
* **bad/type** contains programs with type errors.
* **execute** contains programs that should compile and run without problems,
  and which have expected output.

The tests in **shared** are the tests that we will submit as part of the
project examination.
