package mjc.symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import mjc.types.Type;

import com.google.common.collect.ArrayListMultimap;

/**
 * MethodInfo represents information about a declared method.
 */
public class MethodInfo {

    private final String name;
    private final Type returnType;

    private final List<VariableInfo> parameters;
    private final ArrayListMultimap<String, VariableInfo> locals;

    private final Stack<Integer> blocks;

    private final int line;
    private final int column;

    private int nextBlock = 0;

    /**
     * Construct a new MethodInfo.
     *
     * @param name Name of the method.
     * @param returnType Return type of the method.
     * @param line Line of declaration.
     * @param column Column of declaration.
     */
    public MethodInfo(final String name, final Type returnType, int line, int column) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.locals = ArrayListMultimap.create();
        this.returnType = returnType;
        this.blocks = new Stack<>();
        this.line = line;
        this.column = column;
    }

    /**
     * @return The method name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The method return type.
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * Returns information about a parameter of the method.
     *
     * Since parameters are always visible within a method, the return value of
     * this method is unaffected by calls to {@link #enterBlock() enterBlock} and
     * {@link #leaveBlock() leaveBlock}.
     *
     * @param name Parameter name.
     * @return Parameter information, or null if method has no such parameter.
     */
    public VariableInfo getParameter(final String name) {
        for (VariableInfo info : parameters) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        return null;
    }

    /**
     * @return List of method parameters.
     */
    public List<VariableInfo> getParameters() {
        return parameters;
    }

    /**
     * Adds information about a parameter of the method.
     *
     * The information is added to the end of the list of parameters.
     *
     * @param parameter Information about the parameter.
     * @return The added VariableInfo.
     */
    public VariableInfo addParameter(final VariableInfo parameter) {
        parameter.setBlock(0);
        parameters.add(parameter);
        return parameter;
    }

    /**
     * Returns information about a currently visible local variable.
     *
     * The return value of this method depends on previous calls to
     * {@link #enterBlock() enterBlock} and {@link #leaveBlock leaveBlock}.
     *
     * @param name Variable name.
     * @return Variable information, or null if method has no such local variable or the
     *         local variable is out of scope.
     */
    public VariableInfo getLocal(final String name) {
        for (VariableInfo local : locals.get(name)) {
            if (local.getName().equals(name) && isVisible(local)) {
                return local;
            }
        }
        return null;
    }

    /**
     * @return Collection of local variables of the method.
     */
    public Collection<VariableInfo> getLocals() {
        return locals.values();
    }

    /**
     * Adds information about a local variable declared in the current block.
     *
     * Initially there is no block, so {@link #enterBlock() enterBlock} must have been
     * called more times than {@link #leaveBlock() leaveBlock} before calling this method.
     *
     * @param local Variable information.
     * @return The added VariableInfo.
     * @see enterBlock()
     * @see leaveBlock()
     */
    public VariableInfo addLocal(final VariableInfo local) {
        local.setBlock(blocks.peek());
        locals.put(local.getName(), local);
        return local;
    }

    /**
     * @return Line where the method is declared.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Column where the method is declared.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Enter a new block.
     *
     * This affects subsequent calls to {@link #addLocal(VariableInfo) addLocal} and
     * {@link #getLocal(String) getLocal}. Previously declared variables will remain
     * in scope after this method is called.
     */
    public void enterBlock() {
        blocks.push(nextBlock++);
    }

    /**
     * Leave the current block.
     *
     * This affects subsequent calls to {@link #addLocal(VariableInfo) addLocal} and
     * {@link #getLocal(String) getLocal}. Variables declared in the current block
     * will go out of scope after this method is called.
     */
    public void leaveBlock() {
        blocks.pop();
        if (blocks.isEmpty())
            nextBlock = 0;
    }

    /**
     * Returns true if the local variable @a local is currently visible.
     *
     * @param local A local variable of this method.
     * @return true if @a local is visible, otherwise false.
     */
    private boolean isVisible(final VariableInfo local) {
        return blocks.search(local.getBlock()) != -1;
    }

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (VariableInfo param : parameters) {
            params.append(String.format("%s %s, ", param.getType(), param.getName()));
        }
        if (!parameters.isEmpty()) {
            // Remove trailing ", ".
            params.deleteCharAt(params.length() - 1);
            params.deleteCharAt(params.length() - 1);
        }
        return String.format("%s %s(%s)", returnType, name, params);
    }
}
