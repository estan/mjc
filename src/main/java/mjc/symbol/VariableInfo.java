package mjc.symbol;

import mjc.types.Type;
import mjc.types.UndefinedType;

public class VariableInfo {
    public static final VariableInfo Undefined = new VariableInfo("$$$UndefinedVariableInfo$$$", UndefinedType.Instance, 0, 0);

    private final String name;
    private final Type type;

    private final int line;
    private final int column;

    private int block;

    public VariableInfo(String name, Type type, int line, int column) {
        this.name = name;
        this.type = type;
        this.block = -1;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getBlock() {
        return block;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return name + " (type: " + type + ", block: " + block + ", line: " + line + ", column: " + column + ")";
    }
}
