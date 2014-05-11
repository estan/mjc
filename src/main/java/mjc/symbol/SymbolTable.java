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
}
