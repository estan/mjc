MiniJava Compiler (`mjc`)
=========================

This is a compiler for a slightly modified version of the MiniJava
language described in Appel's *Modern Compiler Implementation in Java*.
It was created as part of the course DD2488 Compiler Construction at
KTH.

Building / Running
------------------

Type `ant` in the top-level directory. This will build the compiler
and unit tests, and produce `mjc.jar`. To run the compiler, type

    java -cp mjc.jar:lib/* mjc.ARMMain foo.java -S

where `foo.java` is the MiniJava source file to be compiled.

See `ant -projecthelp` for the different available targets.

### In Eclipse

To get started in Eclipse, make an initial build with Ant by typing `ant`,
then perform the following steps in a new or existing Eclipse workspace:

1. **File → New → Java Project**.
2. Enter **mjc** as Project name.
3. Uncheck **Use default location** and pick the **mjc** folder as **Location**.
4. Click **Next** and then **Finish**.

It's important that the output folder for each source folder matches
the Ant configuration. To accomplish this:

1. In the project properties for the **mjc** project, under
   **Java Build Path → Source**:
  1. Check the **Allow output folders for source folders** checkbox.
  2. Change the **Target folder** for each of the source folders to
    * **build/parser-classes** for **build/parser-src**
    * **build/main-classes** for **mjc/src/main/java**, and
    * **build/test-classes** for **mjc/src/test/java**.
  3. Click **OK**.

The sources in **build/parser-src** generated by SableCC have some warnings.
To silence them, in the properties for the **build/parser-src** source folder,
under **Java Compiler** check the checkbox **Ignore optional compile problems**.

That's it!
