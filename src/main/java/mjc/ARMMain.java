package mjc;

import java.io.PushbackReader;
import java.io.FileReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import mjc.lexer.Lexer;
import mjc.parser.Parser;
import mjc.symbol.SymbolTable;
import mjc.symbol.SymbolTableBuilder;
import mjc.node.Start;
import mjc.analysis.ASTGraphPrinter;
import mjc.analysis.ASTPrinter;
import mjc.analysis.TypeChecker;
import mjc.error.MiniJavaError;

public class ARMMain {
    private final ASTPrinter astPrinter = new ASTPrinter();
    private final ASTGraphPrinter graphPrinter = new ASTGraphPrinter();
    private final CommandLineParser commandLineParser = new GnuParser();
    private final HelpFormatter helpFormatter = new HelpFormatter();
    private final Options options = new Options();

    private int run(String[] args) throws Exception {
        options.addOption("S", false, "output assembler code");
        options.addOption("o", true, "output file");
        options.addOption("h", false, "show help message");
        options.addOption("p", false, "print abstract syntax tree");
        options.addOption("g", false, "print abstract syntax tree (GraphViz)");

        final CommandLine commandLine = commandLineParser.parse(options, args);

        if (commandLine.hasOption("h") || commandLine.getArgs().length != 1) {
            helpFormatter.printHelp("mjc infile [options]", options);
            System.exit(1);
        }

        /******************************
         * Stage 1: Lexing + Parsing. *
         *****************************/

        final String fileName = commandLine.getArgs()[0];
        final PushbackReader reader = new PushbackReader(new FileReader(fileName));
        final Parser parser = new Parser(new Lexer(reader));
        final Start tree = parser.parse();

        if (commandLine.hasOption("p")) {
            // Print AST and abort.
            astPrinter.print(tree);
            return 0;
        }

        if (commandLine.hasOption("g")) {
            // Print AST in GraphViz format and abort.
            graphPrinter.print(tree);
            return 0;
        }

        /*******************************
         * Stage 2: Semantic Analysis. *
         *******************************/

        // Build symbol table.
        final SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        final SymbolTable symbolTable = symbolTableBuilder.build(tree);
        if (symbolTableBuilder.hasErrors()) {
            for (MiniJavaError error : symbolTableBuilder.getErrors()) {
                System.err.println(error);
            }
        }

        // Run type-check.
        final TypeChecker typeChecker = new TypeChecker();
        if (!typeChecker.check(tree, symbolTable)) {
            for (MiniJavaError error : typeChecker.getErrors()) {
                System.err.println(error);
            }
        }

        if (symbolTableBuilder.hasErrors() || typeChecker.hasErrors()) {
            // Errors in symbol table building or type checking, abort.
            return 1;
        }

        return 0;
    }

    public static void main(String[] args) {
        ARMMain program = new ARMMain();
        try {
            System.exit(program.run(args));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
