package mjc;

import java.io.PushbackReader;
import java.io.FileReader;

import mjc.lexer.Lexer;
import mjc.parser.Parser;
import mjc.node.Start;
import mjc.analysis.LogPrintVisitor;
import mjc.analysis.BasicAnalysisVisitor;

public class ARMMain {
    private BasicAnalysisVisitor basicAnalysisVisitor = new BasicAnalysisVisitor();
    private LogPrintVisitor logPrintVisitor = new LogPrintVisitor();

    private void run(String[] args) {
        Start tree;
        try {
            // Parse input from the specified file.
            PushbackReader in = new PushbackReader(new FileReader(args[0]));
            Parser parser = new Parser(new Lexer(in));
            tree = parser.parse();

            // Apply basic analysis.
            tree.apply(basicAnalysisVisitor);
            if (!basicAnalysisVisitor.success())
                return;

            // Print AST to log.
            tree.apply(logPrintVisitor);

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
