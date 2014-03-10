package mjc.types;

/**
 * Abstract base class for MiniJava types.
 */
public abstract class Type {
    /**
     * Returns true if this is a built-in type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isBuiltIn() {
        return false;
    }

    /**
     * Returns true if this is a user-defined class type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isClass() {
        return false;
    }

    /**
     * Returns true if instances of this type can be assigned to instances of @a type.
     *
     * The default implementation returns true if @a type is the same instance as this,
     * or if @a type is UndefinedType.Instance.
     *
     * @param type A type.
     * @return true if instances of this type can be assigned to instances of @a type.
     */
    public boolean isAssignableTo(Type type) {
        return type == this || type == UndefinedType.Instance;
    }
}
