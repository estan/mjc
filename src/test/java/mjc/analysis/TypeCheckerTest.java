package mjc.analysis;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mjc.analysis.TypeChecker;
import mjc.lexer.Lexer;
import mjc.lexer.LexerException;
import mjc.node.Start;
import mjc.parser.Parser;
import mjc.parser.ParserException;
import mjc.symbol.SymbolTable;
import mjc.error.MiniJavaErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.containsInAnyOrder;;

/**
 * Tests the type checker on both valid input and input with type errors.
 *
 * The test case will run one on each input file Foo.java in dataDirs. If the test
 * finds a corresponding Foo.expected containing expected type errors, it will check
 * that the TypeChecker found the expected errors (and only those). If there is no
 * corresponding Foo.expected, the test will check that the TypeChecker found no
 * errors.
 *
 * E.g. a check that assignment of long to int fails as expected would have
 *
 * <code>
 * class Test {
 *     public static void main(String[] args) {
 *         long l;
 *         int i;
 *         i = l; // Bad! Assignment of long to int.
 *     }
 * }
 * </code>
 *
 * in Test.java and
 *
 * <code>
 * INVALID_ASSIGNMENT
 * </code>
 *
 * in the corresponding Test.expected. The names in the .expected file refer to the
 * enum values in MiniJavaErrorType. There can be multiple such values in each
 * .expected, one for each expected type error, and the order does not matter; as
 * long as all the listed errors are reported by the checker (and only those), the
 * test will pass.
 */
@RunWith(Parameterized.class)
public class TypeCheckerTest {
    private static String[] dataDirs = {
        "src/test/resources/compile",
        "src/test/resources/noncompile/type"
    };

    private String path; // Set once for each file in dataDir.

    /**
     * Create a new test case for the file at the given path.
     *
     * @param path Path of file to test on.
     */
    public TypeCheckerTest(String path) {
        this.path = path;
    }

    /**
     * Tests that the type checker finds the expected problems.
     *
     * @throws IOException if an I/O error occurred.
     * @throws ParserException if parsing failed.
     * @throws LexerException if lexical analysis failed.
     */
    @Test
    public void testCheck() throws IOException, ParserException, LexerException {
        // Parse input.
        FileReader reader = new FileReader(path);
        Parser parser = new Parser(new Lexer(new PushbackReader(reader)));
        Start tree = parser.parse();
        reader.close();

        // Build symbol table and assert there were no errors.
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        SymbolTable symbolTable = symbolTableBuilder.build(tree);
        assertThat(symbolTableBuilder.getErrors(), is(emptyIterable()));

        // Run type-checker and assert that the errors are the expected ones.
        TypeChecker typeChecker = new TypeChecker();
        typeChecker.check(tree, symbolTable);

        try {
            List<MiniJavaErrorType> expectedErrors = readExpected(path);

            // Assert that we got the expected errors.
            assertThat(typeChecker.getErrors(), containsInAnyOrder(expectedErrors.toArray()));
        } catch (IOException e) {
            // No errors expected: Assert that the are none.
            assertThat(typeChecker.getErrors(), is(emptyIterable()));
        }
    }

    /**
     * Provides the absolute path of each *.java file in dataDir as input to
     * testCheck().
     *
     * @return an iterable over paths.
     * @throws IOException if an I/O error occurred.
     */
    @Parameters(name = "{0}")
    public static Iterable<Object[]> testValidData() throws IOException {
        ArrayList<Object[]> data = new ArrayList<>();
        for (String dataDir : dataDirs) {
            for (Path path : Files.newDirectoryStream(Paths.get(dataDir), "*.java")) {
                data.add(new Object[] { path.toAbsolutePath().toString() });
            }
        }
        return data;
    }

    /**
     * Helper method.
     *
     * Given the path of a .java file, reads the list of expected errors from the corresponding
     * .expected file.
     *
     * @param javaPath Path to the .java file (e.g. "src/resources/type_error/Foo.java")
     * @return List of expected errors found in e.g. "src/resources/type_error/Foo.expected".
     * @throws IOException If an I/O error occurred.
     */
    private List<MiniJavaErrorType> readExpected(String javaPath) throws IOException {
        FileReader reader = new FileReader(path.substring(0, path.length() - 5) + ".expected");
        Scanner scanner = new Scanner(reader);

        List<MiniJavaErrorType> expectedErrors = new ArrayList<>();
        while (scanner.hasNext()) {
            expectedErrors.add(MiniJavaErrorType.valueOf(scanner.next()));
        }

        scanner.close();
        reader.close();

        return expectedErrors;
    }
}
