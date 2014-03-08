package mjc.types;

import mjc.symbol.ClassInfo;

public class UndefinedType extends ClassType {
    public final static ClassType Instance = new UndefinedType();

    public final static String Name = "$$$UndefinedType$$$";
    public final static ClassInfo Info = new ClassInfo(Name, Instance);

    private UndefinedType() {
        super(Name, Info);
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

    @Override
    public String toString() {
        return Name;
    }
}
