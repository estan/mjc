package mjc.symbol;

import mjc.types.Type;

/**
 * VariableInfo represents information about a declared variable.
 */
public class VariableInfo {
    private final String name;
    private final Type type;

    private final int line;
    private final int column;

    private int block; // Only used for local variables.

    /**
     * Constructs a new VariableInfo.
     *
     * @param name Name of the variable.
     * @param type Type of the variable.
     * @param line Line of declaration.
     * @param column Column of declaration.
     */
    public VariableInfo(String name, Type type, int line, int column) {
        this.name = name;
        this.type = type;
        this.block = -1;
        this.line = line;
        this.column = column;
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
     * Returns the block within the method in which a local variable is declared,
     * or -1 if the variable is a field or parameter.
     *
     * @return Block of the local variable, or -1 if it is a field or parameter.
     */
    public int getBlock() {
        return block;
    }

    /**
     * Sets the block within the method in which a local variable is declared.
     *
     * Note: This should really only be called by {@link MethodInfo#addLocal(VariableInfo)}.
     *
     * @param block The block in which the local variable is declared.
     */
    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
