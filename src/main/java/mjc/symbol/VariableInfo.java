package mjc.symbol;

import mjc.types.Type;

/**
 * VariableInfo represents information about a declared variable/field/parameter.
 */
public class VariableInfo {
    private final String name;
    private final Type type;

    private final int line;
    private final int column;

    private int block;

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
     * Returns the block within the method in which the variable is declared,
     * or -1 if the variable is a field.
     *
     * @return Block of the variable, or -1 if it is a field.
     */
    public int getBlock() {
        return block;
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
     * Set the block within the method in which the variable is declared.
     *
     * Note: This should really only be called by {@link MethodInfo#addLocal addLocal}.
     *
     * @param block The block in which the variable is declared.
     */
    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return name + " (type: " + type + ", block: " + block + ", line: " + line + ", column: " + column + ")";
    }
}
