package mjc.symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mjc.types.ClassType;

public class ClassInfo {

    private final String name;
    private final ClassType type;
    private final Map<String, VariableInfo> fields;
    private final Map<String, MethodInfo> methods;

    private final int line;
    private final int column;

    public ClassInfo(String name, ClassType type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;

        fields = new HashMap<>();
        methods = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public ClassType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public VariableInfo getField(String name) {
        return fields.get(name);
    }

    public Collection<VariableInfo> getFields() {
        return fields.values();
    }

    public void addField(VariableInfo field) {
        fields.put(field.getName(), field);
    }

    public MethodInfo getMethod(String name) {
        return methods.get(name);
    }

    public Collection<MethodInfo> getMethods() {
        return methods.values();
    }

    public void addMethod(String name, MethodInfo info) {
        methods.put(name, info);
    }

    @Override
    public String toString() {
        return "[Class] " + name;
    }
}
