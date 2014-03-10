package mjc.types;

import mjc.node.ABooleanType;
import mjc.node.AIntArrayType;
import mjc.node.AIntType;
import mjc.node.ALongArrayType;
import mjc.node.ALongType;
import mjc.node.PType;

public class BuiltInType extends Type {

    public final static Type Integer = new BuiltInType("int");
    public final static Type Long = new BuiltInType("long");
    public final static Type Boolean = new BuiltInType("boolean");
    public final static Type IntegerArray = new BuiltInType("int[]");
    public final static Type LongArray = new BuiltInType("long[]");

    private final String name;

    private BuiltInType(String name) {
        this.name = name;
    }

    @Override
    public boolean isBuiltIn() {
        return true;
    }

    @Override
    public boolean isClass() {
        return false;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Type fromAbstract(PType abstractType) {
        if (abstractType instanceof AIntType) {
            return BuiltInType.Integer;
        } else if (abstractType instanceof AIntArrayType) {
            return BuiltInType.IntegerArray;
        } else if (abstractType instanceof ALongType) {
            return BuiltInType.Long;
        } else if (abstractType instanceof ALongArrayType) {
            return BuiltInType.LongArray;
        } else if (abstractType instanceof ABooleanType) {
            return BuiltInType.Boolean;
        } else {
            return UndefinedType.Instance;
        }
    }
}
