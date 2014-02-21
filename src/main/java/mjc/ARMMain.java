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

            // Run basic analysis.
            if (!analyzer.analyze(tree)) {
                System.exit(1);
            }

            // Print AST to log.
            if (commandLine.hasOption("p")) {
                astPrinter.print(tree);
            } else if (commandLine.hasOption("g")) {
                // Print AST in GraphViz format.
                graphPrinter.print(tree);
            } else {
                // Lets just print "42", code courtesy of GCC :)
                System.out.println(
                    "        .arch armv6\n" +
                    "        .eabi_attribute 27, 3\n" +
                    "        .eabi_attribute 28, 1\n" +
                    "        .fpu vfp\n" +
                    "        .eabi_attribute 20, 1\n" +
                    "        .eabi_attribute 21, 1\n" +
                    "        .eabi_attribute 23, 3\n" +
                    "        .eabi_attribute 24, 1\n" +
                    "        .eabi_attribute 25, 1\n" +
                    "        .eabi_attribute 26, 2\n" +
                    "        .eabi_attribute 30, 6\n" +
                    "        .eabi_attribute 34, 1\n" +
                    "        .eabi_attribute 18, 4\n" +
                    "        .file   \"test.c\"\n" +
                    "        .text\n" +
                    "        .align  2\n" +
                    "        .global main\n" +
                    "        .type   main, %function\n" +
                    "main:\n" +
                    "        @ args = 0, pretend = 0, frame = 0\n" +
                    "        @ frame_needed = 1, uses_anonymous_args = 0\n" +
                    "        stmfd   sp!, {fp, lr}\n" +
                    "        add     fp, sp, #4\n" +
                    "        mov     r0, #42\n" +
                    "        bl      _minijavalib_println\n" +
                    "        mov     r0, r3\n" +
                    "        ldmfd   sp!, {fp, pc}\n" +
                    "        .size   main, .-main\n" +
                    "        .ident  \"GCC: (GNU) 4.7.2\"\n" +
                    "        .section        .note.GNU-stack,\"\",%progbits");
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
