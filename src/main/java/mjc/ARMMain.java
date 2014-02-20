package mjc;

import java.io.PushbackReader;
import java.io.FileReader;

import mjc.lexer.Lexer;
import mjc.parser.Parser;
import mjc.node.Start;
import mjc.analysis.ASTPrinter;
import mjc.analysis.BasicAnalyzer;

public class ARMMain {
    private BasicAnalyzer analyzer = new BasicAnalyzer();
    private ASTPrinter printer = new ASTPrinter();

    private void run(String[] args) {
        try {
            // Parse input from the specified file.
            PushbackReader in = new PushbackReader(new FileReader(args[0]));
            Parser parser = new Parser(new Lexer(in));
            Start tree = parser.parse();

            // Run basic analysis.
            if (!analyzer.analyze(tree))
                return;

            // Print AST to log.
            printer.print(tree);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java mjc.ARMMain <file>");
            System.exit(1);
        }

        ARMMain program = new ARMMain();
        program.run(args);
    }
}
