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
     * Adds a Jasmin descriptor to the writer.
     *
     * @param descriptor Descriptor to add, including any format specifiers.
     * @param args Arguments matching format specifiers in @a descriptor.
     */
    void descriptor(String descriptor, Object... args) {
        out.append('.' + String.format(descriptor, args) + '\n');
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
     * Adds a Jasmin label to the writers.
     *
     * @param label Label to add.
     */
    void label(String label) {
        out.append(label + ":\n");
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
     * Clears any statements added Jasmin statements.
     */
    void clear() {
        out.setLength(0);
    }
}
