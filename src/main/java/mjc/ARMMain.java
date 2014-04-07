package mjc;

import java.io.File;
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

import com.google.common.io.Files;

import mjc.lexer.Lexer;
import mjc.lexer.LexerException;
import mjc.parser.Parser;
import mjc.parser.ParserException;
import mjc.symbol.SymbolTable;
import mjc.node.InvalidToken;
import mjc.node.Start;
import mjc.analysis.ASTGraphPrinter;
import mjc.analysis.ASTPrinter;
import mjc.analysis.SymbolTableBuilder;
import mjc.analysis.TypeChecker;
import mjc.error.MiniJavaError;
import static mjc.error.MiniJavaErrorType.LEXER_ERROR;
import static mjc.error.MiniJavaErrorType.PARSER_ERROR;

public class ARMMain {
    private final static CommandLineParser commandLineParser = new GnuParser();
    private final static HelpFormatter helpFormatter = new HelpFormatter();
    private final static Options options = new Options();

    private final ASTPrinter astPrinter = new ASTPrinter();
    private final ASTGraphPrinter graphPrinter = new ASTGraphPrinter();

    public ARMMain() {
        Option outputFileOption = new Option("o", true, "output file");
        outputFileOption.setArgName("outfile");

        options.addOption("S", false, "output assembly code");
        options.addOption(outputFileOption);
        options.addOption("p", false, "print abstract syntax tree");
        options.addOption("g", false, "print abstract syntax tree in GraphViz format");
        options.addOption("s", false, "print symbol table");
        options.addOption("h", false, "show help message");

        helpFormatter.setOptionComparator(new OptionComparator<Option>());
    }

    public static void main(String[] args) {
        ARMMain main = new ARMMain();

        try {
            // Run the compiler.
            if (!main.run(args)) {
                System.exit(1);
            }
        } catch (ParseException e) {
            printHelp();
            System.exit(1);
        }
    }

    /**
     * Run compiler with the given command line arguments.
     *
     * @param args Command line arguments.
     * @return true if compilation succeeded, otherwise false.
     * @throws ParseException if parsing of command line arguments failed.
     */
    private boolean run(String[] args) throws ParseException {

        final CommandLine commandLine = commandLineParser.parse(options, args);

        if (commandLine.hasOption("h")) {
            printHelp();
            return true;
        }

        if (commandLine.getArgs().length != 1) {
            printHelp();
            return false;
        }

        /****************************************
         * Stage 1: Lexical Analysis / Parsing. *
         ***************************************/

        Start tree = new Start();
        try {
            final String fileName = commandLine.getArgs()[0];
            final PushbackReader reader = new PushbackReader(new FileReader(fileName));
            final Parser parser = new Parser(new Lexer(reader));
            tree = parser.parse();
        } catch (LexerException e) {
            final InvalidToken token = e.getToken();
            final int line = token.getLine();
            final int column = token.getPos();
            System.err.println(LEXER_ERROR.on(line, column, token.getText()));
            return false;
        } catch (ParserException e) {
            System.err.println(PARSER_ERROR.on(e.getLine(), e.getPos(), e.getError()));
            return false;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }

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
            return false; // Abort compilation.
        }

        // Just write an empty output file for now.
        final String baseName = Files.getNameWithoutExtension(commandLine.getArgs()[0]);
        File out = new File(baseName + ".s");
        try {
            out.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    private static void printHelp() {
        helpFormatter.printHelp("mjc <infile> [options]", options);
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
