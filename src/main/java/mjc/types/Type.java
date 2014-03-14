package mjc.types;

/**
 * Abstract base class for MiniJava types.
 */
public abstract class Type {
    /**
     * Returns the name of the type.
     *
     * This must be overridden by sub-classes.
     */
    public abstract String getName();

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
     * Returns true if this is the int type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isInt() {
        return false;
    }

    /**
     * Returns true if this is the long type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isLong() {
        return false;
    }

    /**
     * @return true if this is the int or long type.
     */
    public boolean isInteger() {
        return isInt() || isLong();
    }

    /**
     * Returns true if this is the int array type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isIntArray() {
        return false;
    }

    /**
     * Returns true if this is the long array type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isLongArray() {
        return false;
    }

    /**
     * @return true if this is the int array or long array type.
     */
    public boolean isArray() {
        return isIntArray() || isLongArray();
    }

    /**
     * Returns true if this is the boolean type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isBoolean() {
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
     * Returns true if this is the undefined type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isUndefined() {
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
        return type == this || type.isUndefined();
    }
}
