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

public class ARMMain {
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

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.hasOption("h") || commandLine.getArgs().length != 1) {
                helpFormatter.printHelp("mjc infile [options]", options);
                System.exit(1);
            }

            // Parse input from the specified file.
            PushbackReader in = new PushbackReader(new FileReader(commandLine.getArgs()[0]));
            Parser parser = new Parser(new Lexer(in));
            Start tree = parser.parse();

            if (commandLine.hasOption("p")) {
                // Print AST.
                astPrinter.print(tree);
            } else if (commandLine.hasOption("g")) {
                // Print AST in GraphViz format.
                graphPrinter.print(tree);
            } else {
                // Build symbol table.
                SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
                SymbolTable symbolTable = symbolTableBuilder.build(tree);
                if (symbolTableBuilder.hasErrors()) {
                    for (String error : symbolTableBuilder.getErrors()) {
                        System.err.println(error);
                    }
                }

                // Run type-check.
                TypeChecker typeChecker = new TypeChecker();
                if (!typeChecker.check(tree, symbolTable)) {
                    for (String error : typeChecker.getErrors()) {
                        System.err.println(error);
                    }
                }

                //System.out.println(symbolTable);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        ARMMain program = new ARMMain();
        program.run(args);
    }
}
