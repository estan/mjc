package mjc.types;

/**
 * Abstract base class for MiniJava types.
 */
public abstract class Type {
    /**
     * @return name of the type.
     */
    public abstract String getName();

    /**
     * @return true if this is a built-in type.
     */
    public boolean isBuiltIn() {
        return false;
    }

    /**
     * @return true if this is the int type.
     */
    public boolean isInt() {
        return false;
    }

    /**
     * @return true if this is the int array type.
     */
    public boolean isIntArray() {
        return false;
    }

    /**
     * @return true if this is the boolean type.
     */
    public boolean isBoolean() {
        return false;
    }

    /**
     * @return true if this is a user-defined class type.
     */
    public boolean isClass() {
        return false;
    }

    /**
     * @return true if this is the undefined type.
     */
    public boolean isUndefined() {
        return false;
    }

    /**
     * @return true if this is a reference type.
     */
    public boolean isReference() {
        return false;
    }

    /**
     * @return true if this type is assignable (=) to @a type.
     */
    public boolean isAssignableTo(final Type type) {
        return false;
    }

    /**
     * @return true if this type is comparable (==, !=) to @a type.
     */
    public boolean isEqualComparableTo(final Type type) {
        return false;
    }

    /**
     * @return true if this type is comparable (<, <=, >, >=) to @a type.
     */
    public boolean isRelationalComparableTo(final Type type) {
        return false;
    }

    /**
     * @return true if this type can be added (+) to @a type.
     */
    public boolean isAddableTo(final Type type) {
        return false;
    }

    /**
     * @return true if this type can be subtracted (-) from @a type.
     */
    public boolean isSubtractableFrom(final Type type) {
        return false;
    }

    /**
     * @return true if this type can be multiplied (*) by @a type.
     */
    public boolean isMultipliableWith(final Type type) {
        return false;
    }

    /**
     * @return true if this type can be combined with @a type using logical OR.
     */
    public boolean isDisjunctableWith(final Type type) {
        return false;
    }

    /**
     * @return true if this type can be combined with @a type using logical AND.
     */
    public boolean isConjunctableWith(final Type type) {
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
