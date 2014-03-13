package mjc.types;

/**
 * UndefinedType represents an undefined type.
 *
 * An UndefinedType is assignable to all other types and is both a built-in type
 * and a user-defined class type at the same time.
 *
 * There is only a single static instance of this class.
 */
public class UndefinedType extends ClassType {
    public final static ClassType Instance = new UndefinedType();

    private UndefinedType() {
        super("$$$UndefinedType$$$");
    }

    @Override
    public String getName() {
        return "$$$UndefinedType$$$";
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
