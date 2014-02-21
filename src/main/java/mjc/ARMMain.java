package mjc;

import java.io.PushbackReader;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import mjc.lexer.Lexer;
import mjc.parser.Parser;
import mjc.node.Start;
import mjc.analysis.ASTGraphPrinter;
import mjc.analysis.ASTPrinter;
import mjc.analysis.BasicAnalyzer;

public class ARMMain {
    private BasicAnalyzer analyzer = new BasicAnalyzer();
    private ASTPrinter astPrinter = new ASTPrinter();
    private ASTGraphPrinter graphPrinter = new ASTGraphPrinter();

    private void run(String[] args) {
        Options options = new Options();
        options.addOption("S", false, "output assembler code");
        options.addOption("o", true, "output file");
        options.addOption("h", false, "show help message");
        options.addOption("p", false, "print abstract syntax tree");
        options.addOption("g", false, "print abstract syntax tree (GraphViz)");

        CommandLineParser commandLineParser = new GnuParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(null);

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("h") || commandLine.getArgs().length != 1) {
                helpFormatter.printHelp("java ARMMain <file> <options>", options);
                System.exit(1);
            }

            // Parse input from the specified file.
            PushbackReader in = new PushbackReader(new FileReader(commandLine.getArgs()[0]));
            Parser parser = new Parser(new Lexer(in));
            Start tree = parser.parse();

            // Run basic analysis.
            if (!analyzer.analyze(tree)) {
                System.exit(1);
            }

            // Print AST to log.
            if (commandLine.hasOption("p")) {
                astPrinter.print(tree);
            }

            // Print AST in GraphViz format.
            if (commandLine.hasOption("g")) {
                graphPrinter.print(tree);
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        ARMMain program = new ARMMain();
        program.run(args);
    }
}
