package mjc.types;

public class UnsupportedType extends Type {
    public final static Type Void = new UnsupportedType("void");
    public final static Type StringArray = new UnsupportedType("String[]");

    private final String name;

    private UnsupportedType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isBuiltIn() {
        return false;
    }

    @Override
    public boolean isClass() {
        return false;
    }

    @Override
    public boolean isAssignableTo(Type o) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
