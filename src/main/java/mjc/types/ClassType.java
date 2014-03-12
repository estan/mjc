package mjc.types;

/**
 * ClassType represents a user-defined class type.
 */
public class ClassType extends Type {
    private final String name;

    /**
     * Construct a new class type.
     *
     * @param name Name of the type.
     */
    public ClassType(String name) {
        this.name = name;
    }

    /**
     * @return Name of the type.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean isBuiltIn() {
        return false;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isAssignableTo(Type type) {
        if (type == this || type == UndefinedType.Instance)
            return true;
        if (type instanceof ClassType && ((ClassType)type).getName().equals(name))
            return true;
        return false;

    }

    @Override
    public String toString() {
        return name;
    }
}
