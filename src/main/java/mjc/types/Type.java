package mjc.types;

import java.util.List;

import mjc.node.ABooleanType;
import mjc.node.AClassType;
import mjc.node.AIntArrayType;
import mjc.node.AIntType;
import mjc.node.ALongArrayType;
import mjc.node.ALongType;
import mjc.node.PType;
import mjc.node.TIdentifier;
import mjc.symbol.ClassInfo;
import mjc.symbol.SymbolTable;

/**
 * Abstract base class for MiniJava types.
 */
public abstract class Type {
    /**
     * Returns the name of the type.
     *
     * This must be overridden by sub-classes.
     */
    public abstract String getName();

    /**
     * Returns true if this is a built-in type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isBuiltIn() {
        return false;
    }

    /**
     * Returns true if this is a user-defined class type.
     *
     * The default implementation returns false.
     *
     * @return false.
     */
    public boolean isClass() {
        return false;
    }

    /**
     * Returns true if instances of this type can be assigned to instances of @a type.
     *
     * The default implementation returns true if @a type is the same instance as this,
     * or if @a type is UndefinedType.Instance.
     *
     * @param type A type.
     * @return true if instances of this type can be assigned to instances of @a type.
     */
    public boolean isAssignableTo(Type type) {
        return type == this || type == UndefinedType.Instance;
    }

    /**
     * Returns the Type corresponding to an AST type.
     *
     * If @a abstractType is of class type, but the class is not declared in the given
     * symbol table, this method returns UndefinedType.Instance and adds an error to
     * the given error list.
     *
     * @param abstractType Input AST type.
     * @param symbolTable Symbol table to use, should the given type be of class type.
     * @param errors Error list to add to, should the type be undeclared.
     * @return The corresponding Type, or UndefinedType.Instance if it is undeclared.
     * @throws RuntimeException if @a abstractType is of unknown PType subclass.
     */
    public static Type fromAbstract(final PType abstractType, final SymbolTable symbolTable,
            final List<String> errors) {
        if (abstractType instanceof AClassType) {
            // AST type is a class type.
            final AClassType classType = (AClassType)abstractType;
            final TIdentifier classId = classType.getName();
            final ClassInfo classInfo = symbolTable.getClassInfo(classId.getText());
            if (classInfo != null) {
                return classInfo.getType();
            } else {
                errors.add(String.format("[%d,%d] undeclared class `%s`",
                        classId.getLine(), classId.getPos(), classId.getText()));
                return UndefinedType.Instance;
            }
        } else if (abstractType instanceof AIntType) {
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
            throw new RuntimeException("Unknown PType");
        }
    }
}
