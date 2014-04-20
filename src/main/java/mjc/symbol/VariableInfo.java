package mjc.symbol;

import mjc.frame.Access;
import mjc.types.Type;

/**
 * VariableInfo represents information about a declared variable.
 */
public class VariableInfo {
    private final String name;
    private final Type type;

    private final int line;
    private final int column;

    private final int index;
    private final int block;

    private Access access;

    /**
     * Constructs a new VariableInfo.
     *
     * @param name Name of the variable.
     * @param type Type of the variable.
     * @param line Line of declaration.
     * @param column Column of declaration.
     * @param index Index within the class for fields, or index within method for
     *              parameters and local variables.
     */
    VariableInfo(String name, Type type, int line, int column, int index, int block) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
        this.index = index;
        this.block = block;
    }

    /**
     * @return Name of the variable.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Type of the variable.
     */
    public Type getType() {
        return type;
    }

    /**
     * @return Line of declaration.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Column of the declaration.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the block in which the variable was declared.
     *
     * This is always 0 for fields and parameters, but may be >= 0 for local variables.
     *
     * @return block in which the variable was declared.
     */
    public int getBlock() {
        return block;
    }

    /**
     * Returns the index of the variable within the class or method.
     *
     * @return the index of the variable.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the Access for this variable.
     *
     * @return the Access for this variable, or null if there is none.
     */
    public Access getAccess() {
        return access;
    }

    /**
     * Sets the Access for this variable.
     *
     * <em>Note: This should only really be called by the {@link Translator} during
     *     translation to IR</em>
     *
     * @param access the Access to set for this variable.
     */
    public void setAccess(Access access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
