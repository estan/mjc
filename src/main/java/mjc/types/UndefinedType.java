package mjc.types;

/**
 * UndefinedType represents an undefined type.
 *
 * The undefined type is compatible with all operations on all other types. In
 * that sense it acts as a "chameleon" type.
 *
 * The undefined type appear during symbol table construction and type checking; As
 * undeclared identifiers or type errors are encountered, the offending expression
 * gets assigned the undefined type, thereby suppressing any further errors about
 * expressions in which it takes part, letting the programmer focus on the real
 * (first) error.
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
    public int getSize() {
        return -1;
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
}
