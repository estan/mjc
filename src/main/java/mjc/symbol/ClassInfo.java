package mjc.symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mjc.types.ClassType;
import mjc.types.UndefinedType;

public class ClassInfo {
    public static final ClassInfo Undefined = new ClassInfo("$$$UndefinedClassInfo$$$", UndefinedType.Instance);

    private final String name;
    private ClassType type;
    private final Map<String, VariableInfo> fields;
    private final Map<String, MethodInfo> methods;

    public ClassInfo(String name) {
        this.name = name;
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public ClassInfo(String name, ClassType type) {
        this(name);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ClassType getType() {
        return type;
    }

    public void setType(ClassType type) {
        this.type = type;
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
