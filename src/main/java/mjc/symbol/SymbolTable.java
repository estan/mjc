package mjc.symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * The SymbolTable holds information about declared symbols in the program.
 *
 * At the top level, it's a map of declared classes.
 */
public class SymbolTable {
    private final Map<String, ClassInfo> classes;

    /**
     * Constructs a new empty symbol table.
     */
    public SymbolTable() {
        classes = new HashMap<>();
    }

    /**
     * Returns information about a declared class.
     *
     * @param name Name of the class.
     * @return Information about the class, or null if there's no such class.
     */
    public ClassInfo getClassInfo(String name) {
        return classes.get(name);
    }

    /**
     * Adds information about a declared class.
     *
     * If information about a class with the same name already exists, it will
     * be replaced.
     *
     * @param name Name of the class.
     * @param info Information about the class.
     */
    public void addClassInfo(String name, ClassInfo info) {
        classes.put(name, info);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[Symbol Table]\n");
        for (Map.Entry<String, ClassInfo> classEntry : classes.entrySet()) {
            final String classKey = classEntry.getKey();
            final ClassInfo classInfo = classEntry.getValue();

            builder.append(String.format("[Class key=%s] %s\n", classKey, classInfo));
            for (Map.Entry<String, VariableInfo> fieldEntry : classInfo.getFields().entrySet()) {
                builder.append("    [Field] " + fieldEntry.getValue().getName() + "\n");
            }
            for (Map.Entry<String, MethodInfo> methodEntry : classInfo.getMethods().entrySet()) {
                final String methodKey = methodEntry.getKey();
                final MethodInfo methodInfo = methodEntry.getValue();
                builder.append(String.format("    [Method key=%s] %s\n", methodKey, methodInfo));
                for (VariableInfo local : methodInfo.getLocals()) {
                    builder.append("      [Local] " + local + "\n");
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
