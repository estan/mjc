package mjc.types;

/**
 * UndefinedType represents an undefined type.
 *
 * An UndefinedType is assignable to all other types.
 *
 * There is only a single static instance of this class.
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
    public boolean isAssignableTo(Type o) {
        return true;
    }
}
