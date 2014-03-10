package mjc.types;

/**
 * UndefinedType represents an undefined type.
 *
 * These appear during symbol table construction and type-checking, since it's
 * useful to let symbols that are wrongly declared, or undeclared, get a type anyway,
 * to let the type-checking proceed.
 *
 * The undefined type is assignable to all other types and is both a built-in type
 * and a used-defined class type at the same time.
 *
 * There is only a single static instance of this class.
 */
public class UndefinedType extends ClassType {
    public final static ClassType Instance = new UndefinedType();

    private UndefinedType() {
        super("$$$UndefinedType$$$");
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isAssignableTo(Type o) {
        return true;
    }
}
