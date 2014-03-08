package mjc.types;

import mjc.symbol.ClassInfo;

public class ClassType extends Type {
    private final String name;
    private final ClassInfo info;

    public ClassType(String name, ClassInfo info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public ClassInfo getInfo() {
        return info;
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
