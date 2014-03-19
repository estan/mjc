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

import mjc.analysis.SymbolTableBuilder;
import mjc.error.MiniJavaErrorType;
import mjc.lexer.Lexer;
import mjc.lexer.LexerException;
import mjc.node.Start;
import mjc.parser.Parser;
import mjc.parser.ParserException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Tests the symbol table builder on invalid input.
 *
 * The test case will run once on each *.java file in dataDir.
 */
@RunWith(Parameterized.class)
public class SymbolTableBuilderTest {
    private static String dataDir = "src/test/resources/noncompile/symbol";

    private String path; // Set once for each file in dataDir.

    /**
     * Create a new test case for the file at the given path.
     *
     * @param path Path of file to test on.
     */
    public SymbolTableBuilderTest(String path) {
        this.path = path;
    }

    /**
     * Tests that the symbol table builder finds problems.
     *
     * @throws IOException if an I/O error occurred.
     * @throws ParserException if parsing failed.
     * @throws LexerException if lexical analysis failed.
     */
    @Test
    public void testBuild() throws IOException, ParserException, LexerException {
        // Parse input.
        FileReader reader = new FileReader(path);
        Parser parser = new Parser(new Lexer(new PushbackReader(reader)));
        Start tree = parser.parse();
        reader.close();

        // Build symbol table and assert there were errors.
        SymbolTableBuilder builder = new SymbolTableBuilder();
        builder.build(tree);
        assertThat(builder.getErrors(), containsInAnyOrder(readExpected(path).toArray()));
    }

    /**
     * Provides the absolute path of each *.java file in dataDir as input to
     * testBuild().
     *
     * @return an iterable over paths.
     * @throws IOException if an I/O error occurred.
     */
    @Parameters(name = "{0}")
    public static Iterable<Object[]> testValidData() throws IOException {
        ArrayList<Object[]> data = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(Paths.get(dataDir), "*.java")) {
            data.add(new Object[] { path.toAbsolutePath().toString() });
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
