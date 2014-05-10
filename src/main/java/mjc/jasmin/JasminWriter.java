package mjc.jasmin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Simple helper class for writing Jasmin pseudo-assembler.
 */
class JasminWriter {
    private StringBuilder out = new StringBuilder();

    /**
     * Adds a Jasmin directive to the writer.
     *
     * @param directive Directive to add, including any format specifiers.
     * @param args Arguments matching format specifiers in @a directive.
     */
    void directive(String directive, Object... args) {
        out.append('.' + String.format(directive, args) + '\n');
    }

    /**
     * Adds a Jasmin instruction to the writer.
     *
     * @param instruction Instruction to add, including any format specifiers.
     * @param args Arguments matching format specifiers in @a instruction.
     */
    void instruction(String instruction, Object... args) {
        out.append("    " + String.format(instruction, args) + '\n');
    }

    /**
     * Adds a Jasmin label to the writer.
     *
     * @param label Label to add.
     */
    void label(String label) {
        out.append(label + ":\n");
    }

    /**
     * Adds an iconst_# or ldc instruction to the writer.
     *
     * @param c Integer constant to push.
     */
    void iconst(int c) {
        instruction(0 <= c && c <= 5 ? "iconst_%d" : "ldc %d", c);
    }

    /**
     * Adds an iload_# or iload instruction to the writer.
     *
     * @param i Variable index.
     */
    void iload(int i) {
        instruction("iload%s", (0 <= i && i <= 3 ? "_" : " ") + i);
    }

    /**
     * Adds an istore_# or istore instruction to the writer.
     *
     * @param i Variable index.
     */
    void istore(int i) {
        instruction("istore%s", (0 <= i && i <= 3 ? "_" : " ") + i);
    }

    /**
     * Adds an aload_# or aload instruction to the writer.
     *
     * @param i Variable index.
     */
    void aload(int i) {
        instruction("aload%s", (0 <= i && i <= 3 ? "_" : " ") + i);
    }

    /**
     * Adds a Jasmin comment to the writer.
     *
     * @param comment A comment, including any format specifiers.
     * @param args Arguments matching format specifiers in @a comment.
     */
    void comment(String comment, Object... args) {
        out.append("; " + String.format(comment, args) + '\n');
    }

    /**
     * Adds a big Jasmin comment to the writer.
     *
     * @param comment A comment, including any format specifiers.
     * @param args Arguments matching format specifiers in @a comment.
     */
    void bigcomment(String comment, Object... args) {
        comment("");
        comment(comment, args);
        comment("");
    }

    /**
     * Adds a newline to the writer.
     */
    void newline() {
        out.append("\n");
    }

    /**
     * Writes the accumulated Jasmin pseudo-assembler code to a stream.
     *
     * @param stream Output stream to write to.
     * @throws IOException If an I/O error occurred.
     */
    void write(OutputStream stream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        writer.append(out);
        writer.flush();
    }

    /**
     * Removes all Jasmin statements that have been added to the writer.
     */
    void clear() {
        out.setLength(0);
    }
}
