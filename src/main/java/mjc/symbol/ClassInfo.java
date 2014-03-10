package mjc.symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mjc.types.ClassType;

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

    /**
     * Construct a new ClassInfo.
     *
     * @param name Name of the class.
     * @param type Type of the class.
     * @param line Line of declaration.
     * @param column Column of declaration.
     */
    public ClassInfo(String name, ClassType type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;

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
    public VariableInfo getField(String name) {
        return fields.get(name);
    }

    /**
     * @return Collection of the fields of the class.
     */
    public Collection<VariableInfo> getFields() {
        return fields.values();
    }

    /**
     * Adds information about a field of the class.
     *
     * If there's already information about a field with the same name, it will be
     * replaced.
     *
     * @param field Information about the field.
     */
    public void addField(VariableInfo field) {
        fields.put(field.getName(), field);
    }

    /**
     * Returns information about a method of the class.
     *
     * @param name Name of the method.
     * @return Information about the method, or null if the class has no such method.
     */
    public MethodInfo getMethod(String name) {
        return methods.get(name);
    }

    /**
     * @return Collection of the methods of the class.
     */
    public Collection<MethodInfo> getMethods() {
        return methods.values();
    }

    /**
     * Adds information about a method of the class.
     *
     * If there's already information about a method with the same name, it will be
     * replaced.
     *
     * @param name Name of the method.
     * @param method Information about the method.
     */
    public void addMethod(String name, MethodInfo info) {
        methods.put(name, info);
    }

    @Override
    public String toString() {
        return "[Class] " + name;
    }
}
