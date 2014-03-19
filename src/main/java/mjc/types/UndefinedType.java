package mjc.types;

/**
 * UndefinedType represents an undefined type.
 *
 * Operations with undefined types are always allowed.
 *
 * There is only one single static instance of this class.
 */
public class UndefinedType extends Type {
    public final static Type Instance = new UndefinedType();

    @Override
    public String getName() {
        return "$$$UndefinedType$$$";
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    @Override
    public boolean isAssignableTo(final Type type) {
        return true;
    }

    @Override
    public boolean isEqualComparableTo(final Type type) {
        return true;
    }

    @Override
    public boolean isRelationalComparableTo(final Type type) {
        return true;
    }

    @Override
    public boolean isAddableTo(final Type type) {
        return true;
    }

    @Override
    public boolean isSubtractableFrom(final Type type) {
        return true;
    }

    @Override
    public boolean isMultipliableWith(final Type type) {
        return true;
    }

    @Override
    public boolean isDisjunctableWith(final Type type) {
        return true;
    }

    @Override
    public boolean isConjunctableWith(final Type type) {
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
