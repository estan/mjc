package mjc.types;

public abstract class Type {
    public boolean isBuiltIn() {
        return false;
    }

    public boolean isClass() {
        return false;
    }

    public boolean isAssignableTo(Type o) {
        return o == this || o == UndefinedType.Instance;
    }
}
