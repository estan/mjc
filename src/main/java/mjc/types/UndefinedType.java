package mjc.types;

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
