package mjc.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import mjc.lexer.Lexer;
import mjc.lexer.LexerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests the parser on valid input.
 *
 * The test case will run once on each *.java file in dataDir.
 */
@RunWith(Parameterized.class)
public class ParserValidInputTest {
    private static String dataDir = "src/test/resources/valid";

    private String path; // Set once for each file in dataDir.

    /**
     * Create a new test case for the file at the given path.
     *
     * @param path Path of file to test on.
     */
    public ParserValidInputTest(String path) {
        this.path = path;
    }

    /**
     * Tests that the parser will parse the input without problems.
     *
     * @throws IOException if an I/O error occurred.
     * @throws ParserException if parsing failed.
     * @throws LexerException if lexical analysis failed.
     */
    @Test
    public void testValid() throws IOException, ParserException, LexerException {
        FileReader reader = new FileReader(path);
        Parser parser = new Parser(new Lexer(new PushbackReader(reader)));
        parser.parse();
        reader.close();
    }

    /**
     * Provides the absolute path of each *.java file in dataDir as input to
     * testValid().
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
}
