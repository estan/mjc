package mjc;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.FileReader;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import mjc.lexer.Lexer;
import mjc.lexer.LexerException;
import mjc.parser.Parser;
import mjc.parser.ParserException;
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

    private final static int EXIT_SUCCESS = 0;
    private final static int EXIT_FAILURE = 1;

    public ARMMain() {
        options.addOption("S", false, "output assembler code");
        options.addOption("o", true, "output file");
        options.addOption("p", false, "print abstract syntax tree");
        options.addOption("g", false, "print abstract syntax tree in GraphViz format");
        options.addOption("s", false, "print symbol table");
        options.addOption("h", false, "show help message");

        helpFormatter.setOptionComparator(new OptionComparator<Option>());
    }

    public static void main(String[] args) {
        ARMMain program = new ARMMain();
        try {
            System.exit(program.run(args));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(EXIT_FAILURE);
        }
    }

    /**
     * Run compiler with the given command line arguments.
     *
     * @param args Command line arguments.
     * @return EXIT_SUCCESS if compilation succeeded, otherwise EXIT_FAILURE.
     *
     * @throws ParseException if parsing of command line arguments failed.
     * @throws IOException if an I/O error occurred.
     * @throws LexerException if lexical analysis failed.
     * @throws ParserException if parsing failed.
     */
    private int run(String[] args) throws ParseException, ParserException, LexerException, IOException {

        final CommandLine commandLine = commandLineParser.parse(options, args);

        if (commandLine.hasOption("h")) {
            helpFormatter.printHelp("mjc <infile> [options]", options);
            System.exit(EXIT_SUCCESS);
        }

        if (commandLine.getArgs().length != 1) {
            helpFormatter.printHelp("mjc <infile> [options]", options);
            System.exit(EXIT_FAILURE);
        }

        /****************************************
         * Stage 1: Lexical Analysis / Parsing. *
         ***************************************/

        final String fileName = commandLine.getArgs()[0];
        final PushbackReader reader = new PushbackReader(new FileReader(fileName));
        final Parser parser = new Parser(new Lexer(reader));
        final Start tree = parser.parse();

        if (commandLine.hasOption("p"))
            astPrinter.print(tree);

        if (commandLine.hasOption("g"))
            graphPrinter.print(tree);

        /*******************************
         * Stage 2: Semantic Analysis. *
         *******************************/

        // Build symbol table.
        final SymbolTableBuilder builder = new SymbolTableBuilder();
        final SymbolTable symbolTable = builder.build(tree);
        if (builder.hasErrors()) {
            for (MiniJavaError error : builder.getErrors()) {
                System.err.println(error);
            }
        }

        if (commandLine.hasOption("s"))
            System.out.println(symbolTable);

        // Run type-check.
        final TypeChecker typeChecker = new TypeChecker();
        if (!typeChecker.check(tree, symbolTable)) {
            for (MiniJavaError error : typeChecker.getErrors()) {
                System.err.println(error);
            }
        }


        if (builder.hasErrors() || typeChecker.hasErrors()) {
            // Errors in symbol table building or type checking, abort.
            return EXIT_FAILURE;
        }

        return EXIT_SUCCESS;
    }

    // Comparator for Options, to get them in the order we want in help output.
    class OptionComparator<T extends Option> implements Comparator<T> {
        private static final String ORDER = "Sopgsh";

        @Override
        public int compare(T option1, T option2) {
            return ORDER.indexOf(option1.getOpt()) - ORDER.indexOf(option2.getOpt());
        }
    }
}
