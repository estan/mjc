package mjc.symbol;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, ClassInfo> classes;

    public SymbolTable() {
        classes = new HashMap<>();
    }

    public ClassInfo getClassInfo(String name) {
        return classes.get(name);
    }

    public void addClassInfo(String name, ClassInfo info) {
        classes.put(name, info);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Symbol Table]\n");
        for (ClassInfo classInfo : classes.values()) {
            builder.append("  [Class] " + classInfo.getName() + "\n");
            for (VariableInfo field : classInfo.getFields()) {
                builder.append("    [Field] " + field.toString() + "\n");
            }
            for (MethodInfo method : classInfo.getMethods()) {
                builder.append("    [Method] " + method.toString() + "\n");
                for (VariableInfo param : method.getParameters()) {
                    builder.append("      [Param] " + param.toString() + "\n");
                }
                for (VariableInfo local : method.getLocals()) {
                    builder.append("      [LocalVar] " + local.toString() + "\n");
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
