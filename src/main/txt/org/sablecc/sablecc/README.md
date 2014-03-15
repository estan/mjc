Custom SableCC parser.txt Template
==================================

This is a slightly modified parser.txt from SableCC. It adds an API
to ParserException to get the error message, line and position of the
error, so that we can customize the error reporting.

We prepend this to the classpath when we invoke SableCC from Ant, so
that our template overrides the one in the SableCC JAR file.
