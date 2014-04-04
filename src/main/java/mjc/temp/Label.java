package mjc.temp;

/**
 * A Label represents an address in assembly language.
 */

public class Label {
    private final String name;
    private static int count;

    /**
     * Makes a new label that prints as "name". Warning: avoid repeated calls to
     * <tt>new Label(s)</tt> with the same name <tt>s</tt>.
     */
    public Label(final String name) {
        this.name = name;
    }

    /**
     * Makes a new label with an arbitrary name.
     */
    public Label() {
        this("L" + count++);
    }

    /**
     * a printable representation of the label, for use in assembly language output.
     */
    @Override
    public String toString() {
        return name;
    }
}
