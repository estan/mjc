package mjc.types;

/**
 * Represents a built-in type.
 *
 * There are only five static instances of this class, one for each of the built-in
 * types int, int[], long, long[] and boolean.
 */
public class BuiltInType extends Type {

    public final static Type Integer = new BuiltInType("int");
    public final static Type Long = new BuiltInType("long");
    public final static Type Boolean = new BuiltInType("boolean");
    public final static Type IntegerArray = new BuiltInType("int[]");
    public final static Type LongArray = new BuiltInType("long[]");

    private final String name;

    /**
     * Construct a new built-in type with the given name.
     *
     * @param name Name of the type.
     */
    private BuiltInType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isAssignableTo(Type type) {
        if (type == this || type == UndefinedType.Instance)
            return true;
        if (this == Integer && type == Long)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
