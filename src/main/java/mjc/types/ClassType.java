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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isReference() {
        return true;
    }

    @Override
    public boolean isAssignableTo(Type type) {
        return type.isUndefined() || type.isClass() && type.getName().equals(name);

    }

    @Override
    public boolean isEqualComparableTo(final Type type) {
        return type.isUndefined() || type.isClass() && type.getName().equals(name);
    }
}
