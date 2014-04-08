package mjc.symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mjc.types.Type;

import com.google.common.collect.ArrayListMultimap;

/**
 * MethodInfo represents information about a declared method.
 *
 * It holds the function name, return type and list of parameters. Local variables are
 * kept in a multimap, since there may be several local variables with the same name but
 * declared in blocks that can't see each other.
 *
 * The scope of added local variables is controlled by calling {@link #enterBlock()} and
 * {@link #leaveBlock()}. The mechanism behind these methods is quite simple, but here's
 * an explanation:
 *
 * Each block within a method is given a number from 0, 1, ..., N as it is encountered.
 *
 * MethodInfo internally maintains a stack of block numbers; each time a block is entered
 * using enterBlock(), a new block number is created from a counter (nextBlock) and pushed
 * on the stack. When leaving a block using leaveBlock(), the stack is popped. When leaving
 * the last block (the stack becomes empty), the nextBlock counter is reset to 0, to prepare
 * it for the next user of the API. Note that this requires users of this API to always make
 * a matching number of calls to enterBlock()/leaveBlock(), to not leave the MethodInfo in
 * an unknown state for the next user.
 *
 * When a local variable is added using {@link #addLocal(VariableInfo)} it gets its block
 * set to the current block (top of the stack) before being added to the multimap.
 *
 * When a local variable is looked up using {@link #getLocal(String)}, the multimap is first
 * queried for the variable with the given name. If the variable is found, we check if its
 * block number is currently on the stack. If it is, the variable is in scope and we return
 * it, otherwise we return null (the variable is not in scope).
 *
 * Local variable lookup is thus O(N * M), where N is the number of local variables with the
 * same name and M is the current block nesting depth. This should be reasonably fast.
 */
public class MethodInfo {

    private final String name;
    private final Type returnType;

    private final List<VariableInfo> parameters;
    private final ArrayListMultimap<String, VariableInfo> locals;

    private final Stack<Integer> blocks;

    private final int line;
    private final int column;

    private int nextOffset = 0;
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
     * Since parameters are always visible within a method, the return value of this method
     * is unaffected by calls to {@link #enterBlock()} and {@link #leaveBlock()}.
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
        parameters.add(parameter);
        return parameter;
    }

    /**
     * Returns information about a currently visible local variable.
     *
     * The return value of this method depends on previous calls to {@link #enterBlock()}
     * and {@link #leaveBlock()}.
     *
     * @param name Variable name.
     * @return Variable information, or null if method has no such local variable or the
     *         local variable is out of scope.
     */
    public VariableInfo getLocal(final String name) {
        for (VariableInfo local : locals.get(name)) {
            if (isVisible(local)) {
                return local;
            }
        }
        return null;
    }

    /**
     * Adds information about a local variable declared in the current block.
     *
     * Initially there is no block, so {@link #enterBlock()} must have been called more
     * times than {@link #leaveBlock()} before calling this method.
     *
     * @param local Variable information.
     * @return The added VariableInfo.
     * @see #enterBlock()
     * @see #leaveBlock()
     */
    public VariableInfo addLocal(final VariableInfo local) {
        local.setOffset(nextOffset);
        local.setBlock(blocks.peek());
        locals.put(local.getName(), local);
        nextOffset += local.getType().getSize();
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
     * This affects subsequent calls to {@link #addLocal(VariableInfo)} and
     * {@link #getLocal(String)}. Previously declared variables will remain in scope
     * after this method is called.
     */
    public void enterBlock() {
        blocks.push(nextBlock++);
    }

    /**
     * Leave the current block.
     *
     * This affects subsequent calls to {@link #addLocal(VariableInfo)} and
     * {@link #getLocal(String)}. Variables declared in the current block will go out
     * of scope after this method is called.
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
        StringBuilder builder = new StringBuilder();
        builder.append(returnType + " " + name + "(");

        // Add parameters.
        for (VariableInfo parameter : parameters) {
            builder.append(parameter + ", ");
        }
        if (!parameters.isEmpty()) {
            // Remove trailing ", ".
            builder.delete(builder.length() - 2, builder.length());
        }

        // Add local variables.
        builder.append(") {");
        for (VariableInfo local : locals.values()) {
            builder.append(local + "; ");
        }
        if (!locals.isEmpty()) {
            // Remove trailing ' '.
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("}");

        return builder.toString();
    }
}
