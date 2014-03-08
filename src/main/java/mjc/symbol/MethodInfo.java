package mjc.symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import mjc.types.Type;
import mjc.types.UndefinedType;

import com.google.common.collect.ArrayListMultimap;

public class MethodInfo {
    public final static MethodInfo Undefined = new MethodInfo("$$$UndefinedMethodInfo$$$", UndefinedType.Instance, 0, 0);

    private final String name;
    private final Type returnType;

    private final List<VariableInfo> parameters;
    private final ArrayListMultimap<String, VariableInfo> locals;

    private final Stack<Integer> blocks;

    private final int line;
    private final int column;

    private int nextBlock = 0;

    public MethodInfo(String name, Type returnType, int line, int column) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.locals = ArrayListMultimap.create();
        this.returnType = returnType;
        this.blocks = new Stack<>();
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public VariableInfo getParameter(String name) {
        if (!blocks.isEmpty()) {
            for (VariableInfo info : parameters) {
                if (info.getName().equals(name)) {
                    return info;
                }
            }
        }
        return null;
    }

    public Collection<VariableInfo> getParameters() {
        return parameters;
    }

    public void addParameter(VariableInfo parameter) {
        parameter.setBlock(0);
        parameters.add(parameter);
    }

    public VariableInfo getLocal(String name) {
        for (VariableInfo local : locals.get(name)) {
            if (local.getName().equals(name) && blocks.search(local.getBlock()) != -1) {
                return local;
            }
        }
        return null;
    }

    public Collection<VariableInfo> getLocals() {
        return locals.values();
    }

    public void addLocal(VariableInfo local) {
        local.setBlock(blocks.peek());
        locals.put(local.getName(), local);
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void enterBlock() {
        blocks.push(nextBlock++);
    }

    public void leaveBlock() {
        blocks.pop();
    }

    @Override
    public String toString() {
        return name + " (return type: " + returnType + ", line: " + line + ", column: " + column + ")";
    }
}
