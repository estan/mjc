package mjc.symbol;

import java.util.HashMap;
import java.util.Map;

import mjc.types.ClassType;
import mjc.types.Type;

/**
 * ClassInfo represents information about a declared class.
 */
public class ClassInfo {

    private final String name;
    private final ClassType type;
    private final Map<String, VariableInfo> fields;
    private final Map<String, MethodInfo> methods;

    private final int line;
    private final int column;

    private int nextIndex;

    /**
     * Construct a new ClassInfo.
     *
     * @param name Name of the class.
     * @param type Type of the class.
     * @param line Line of declaration.
     * @param column Column of declaration.
     */
    public ClassInfo(final String name, final ClassType type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
        this.nextIndex = 1;

        fields = new HashMap<>();
        methods = new HashMap<>();
    }

    /**
     * @return Name of the class.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Type of the class.
     */
    public ClassType getType() {
        return type;
    }

    public int getNumFields() {
        return fields.size();
    }

    /**
     * @return Line of declaration.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return Column of declaration.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns information about a field of the class.
     *
     * @param name Name of the field.
     * @return Information about the field, or null if the class has no such field.
     */
    public VariableInfo getField(final String name) {
        return fields.get(name);
    }

    /**
     * Adds information about a field of the class.
     *
     * If there's already information about a field with the same name, it will be
     * replaced.
     *
     * @param name Name of the field.
     * @param type Type of the field.
     * @param line Line of declaration.
     * @param column Column of declaration.
     * @return the VariableInfo that was added for the field.
     */
    public VariableInfo addField(String name, Type type, int line, int column) {
        VariableInfo field = new VariableInfo(name, type, line, column, nextIndex++, 0);
        fields.put(name, field);
        return field;
    }

    /**
     * Returns information about a method of the class.
     *
     * @param name Name of the method.
     * @return Information about the method, or null if the class has no such method.
     */
    public MethodInfo getMethod(final String name) {
        return methods.get(name);
    }

    /**
     * Adds information about a method of the class.
     *
     * If there's already information about a method with the same name, it will be
     * replaced.
     *
     * @param name Name of the method.
     * @param method Information about the method.
     * @return The added MethodInfo.
     */
    public MethodInfo addMethod(final String name, final MethodInfo method) {
        methods.put(name, method);
        return method;
    }
}
