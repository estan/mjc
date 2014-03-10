package mjc.types;

//import mjc.symbol.ClassInfo;

public class ClassType extends Type {
    private final String name;

    public ClassType(String name) {
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
        return true;
    }

    @Override
    public boolean isAssignableTo(Type o) {
        return equals(o);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this || o == UndefinedType.Instance)
            return true;
        if (o instanceof ClassType && ((ClassType)o).getName().equals(name))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
