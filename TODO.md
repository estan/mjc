MiniJava Compiler TODO
======================

In roughly descending order of priority:

1. Our grammar currently accepts

     int foo = new int[3][2];

   and the natural semantic would be "create an integer array of size 3 and assign
   its third element to foo". In Java, this would be a type error (assignment of
   int[][] to int). Since MiniJava does not have multidimensional arrays, we should
   change our grammar to reject things like this. Look at e.g. Primary and
   PrimaryNoNewArray in the Java 2 LALR grammar for inspiration.
