package mjc.symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AClassType;
import mjc.node.AFieldDeclaration;
import mjc.node.AFormalParameter;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AVariableDeclaration;
import mjc.node.PFormalParameter;
import mjc.node.PType;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.types.BuiltInType;
import mjc.types.ClassType;
import mjc.types.Type;
import mjc.types.UndefinedType;
import mjc.types.UnsupportedType;

/**
 * Symbol table builder.
 *
 * A SymbolTableBuilder builds a symbol table from the AST. It does so in two passes.
 * In the first pass, information about declared classes are added. In the second pass
 * the builder goes deeper and adds information about declared fields, methods and
 * variables.
 *
 * Errors are collected during the building process, and may be queried for using
 * {@link hasErrors() hasErrors} and {@link getErrors() getErrors}.
 *
 * It is OK to proceed with type checking even if errors occurred during symbol table
 * construction: The constructed table is still complete, but will contain Undefined
 * information records for the wrongly declared symbols. However, later phases of
 * compilation should not proceed.
 */
public class SymbolTableBuilder {

    private SymbolTable symbolTable;
    private final List<String> errors = new ArrayList<>();

    /**
     * Builds and returns a symbol table from the given AST.
     *
     * If errors occurred, they may be queried for using {@link hasErrors() hasErrors}
     * and {@link getErrors() getErrors}.
     *
     * @param tree The input AST.
     * @return A symbol table constructed from the AST.
     */
    public SymbolTable build(final Start tree) {
        errors.clear();

        symbolTable = new SymbolTable();

        // Run the two passes to construct table.
        tree.apply(new FirstPass());
        tree.apply(new SecondPass());

        return symbolTable;
    }

    /**
     * Returns true if errors were encountered during symbol table construction.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Returns the list of errors encountered during symbol table construction.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Add an encountered error to the list of errors.
     *
     * args[0] and args[1] should be the line and column, respectively.
     *
     * @param format A formatted string referring to args[2], args[3], ...
     * @param args The line and column, followed by arguments referred to in @a format.
     */
    private void error(String format, Object... args) {
        errors.add(String.format("[%d,%d] error: " + format, args));
    }

    /**
     * Visitor for the first pass of symbol table construction.
     *
     * In the first pass, we only add information about declared classes.
     */
    private class FirstPass extends DepthFirstAdapter {

        @Override
        public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
            final TIdentifier classId = declaration.getName();
            final TIdentifier methodId = declaration.getMainMethodName();

            if (methodId.getText().equals("main")) {
                // Add a new ClassInfo for the main class.
                ClassInfo classInfo = new ClassInfo(classId.getText());
                ClassType classType = new ClassType(classId.getText(), classInfo);
                classInfo.setType(classType);
                symbolTable.addClassInfo(classInfo.getName(), classInfo);
            } else {
                // Wrong method name: Add class as Undefined.
                final int line = methodId.getLine();
                final int column = methodId.getPos();
                symbolTable.addClassInfo(classId.getText(), ClassInfo.Undefined);
                error("name of method in class `%s` must be \"main\"",
                        line, column, classId.getText());
            }

        }

        @Override
        public void inAClassDeclaration(final AClassDeclaration declaration) {
            final TIdentifier id = declaration.getName();

            if (symbolTable.getClassInfo(id.getText()) == null) {
                // Add a new ClassInfo for the class.
                ClassInfo classInfo = new ClassInfo(id.getText());
                ClassType classType = new ClassType(id.getText(), classInfo);
                classInfo.setType(classType);
                symbolTable.addClassInfo(classInfo.getName(), classInfo);
            } else {
                // Redeclaration: Add class as Undefined.
                final int line = id.getLine();
                final int column = id.getPos();
                symbolTable.addClassInfo(id.getText(), ClassInfo.Undefined);
                error("duplicate class `%s`", line, column, id.getText());
            }
        }

    } // End FirstPass

    /**
     * Visitor for the second pass of symbol table construction.
     *
     * In the second pass, we add information about declared fields, methods and
     * local variables.
     */
    private class SecondPass extends DepthFirstAdapter {

        private ClassInfo currentClass;
        private MethodInfo currentMethod;

        @Override
        public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
            currentClass = symbolTable.getClassInfo(declaration.getName().getText());

            // Create MethodInfo for main method.
            final TIdentifier methodId = declaration.getMainMethodName();
            final int methodLine = methodId.getLine();
            final int methodColumn = methodId.getPos();

            final MethodInfo methodInfo = new MethodInfo(
                    methodId.getText(),
                    UnsupportedType.Void,
                    methodLine, methodColumn);

            // Create VariableInfo for main method parameter.
            final TIdentifier paramId = declaration.getArguments();
            final int paramLine = paramId.getLine();
            final int paramColumn = paramId.getPos();

            methodInfo.addParameter(new VariableInfo(
                    paramId.getText(),
                    UnsupportedType.StringArray,
                    paramLine, paramColumn));

            // Add MethodInfo to current (main) class.
            currentClass.addMethod(methodInfo.getName(), methodInfo);

            // Set main method as current method and enter its block.
            currentMethod = methodInfo;
            currentMethod.enterBlock();
        }

        @Override
        public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
            // Leave the main method block.
            currentMethod.leaveBlock();
        }

        @Override
        public void inAClassDeclaration(final AClassDeclaration declaration) {
            currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        }

        @Override
        public void inAFieldDeclaration(final AFieldDeclaration declaration) {
            final TIdentifier fieldId = declaration.getName();
            final int line = fieldId.getLine();
            final int column = fieldId.getPos();

            if (currentClass.getField(fieldId.getText()) == null) {
                // Add new VariableInfo for the field.
                currentClass.addField(new VariableInfo(
                        fieldId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
                // Redeclaration: Add field as Undefined.
                currentClass.addField(VariableInfo.Undefined);
                error("duplicate field `%s`", line, column, fieldId.getText());
            }
        }

        @Override
        public void inAMethodDeclaration(final AMethodDeclaration declaration) {
            currentMethod = currentClass.getMethod(declaration.getName().getText());

            final TIdentifier methodId = declaration.getName();
            final int line = methodId.getLine();
            final int column = methodId.getPos();

            // Determine if this is a redeclaration.
            final MethodInfo otherInfo = currentClass.getMethod(methodId.getText());
            boolean redeclaration = false;
            if (otherInfo != null) { // Another method with the same name exists.
                final List<PFormalParameter> params = declaration.getFormalParameters();
                final List<VariableInfo> otherParams = otherInfo.getParameters();

                // If parameter lists are equal, this is a redeclaration.
                if (params.size() == otherParams.size()) {
                    Iterator<PFormalParameter> it = declaration.getFormalParameters().iterator();
                    Iterator<VariableInfo> otherIt = otherInfo.getParameters().iterator();

                    redeclaration = true;
                    while (it.hasNext()) {
                        AFormalParameter param = (AFormalParameter) it.next();
                        VariableInfo otherParam = otherIt.next();
                        if (!resolve(param.getType()).equals(otherParam.getType())) {
                            // Type of at least one parameter differs.
                            redeclaration = false;
                            break;
                        }
                    }
                }
            }

            if (!redeclaration) {
                // Create MethodInfo for method.
                currentMethod = new MethodInfo(methodId.getText(),
                        resolve(declaration.getReturnType()),
                        line, column);
            } else {
                // Redeclaration: Create method as Undefined.
                currentMethod = MethodInfo.Undefined;
                error("duplicate method `%s`", line, column, methodId.getText());
            }

            // Add method to current class and enter method block.
            currentClass.addMethod(methodId.getText(), currentMethod);
            currentMethod.enterBlock();
        }

        @Override
        public void outAMethodDeclaration(final AMethodDeclaration declaration) {
            // Leave method block.
            currentMethod.leaveBlock();
        }

        @Override
        public void inAFormalParameter(final AFormalParameter declaration) {
            final TIdentifier paramId = declaration.getName();
            final int line = paramId.getLine();
            final int column = paramId.getPos();

            if (currentMethod.getParameter(paramId.getText()) == null) {
                // Add new VariableInfo for parameter.
                currentMethod.addParameter(new VariableInfo(
                        paramId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
                // Redeclaration: Add parameter as Undefined.
                currentMethod.addParameter(VariableInfo.Undefined);
                error("duplicate parameter `%s`", line, column, paramId.getText());
            }
        }

        @Override
        public void inAVariableDeclaration(final AVariableDeclaration declaration) {
            final TIdentifier variableId = declaration.getName();
            final int line = variableId.getLine();
            final int column = variableId.getPos();

            final VariableInfo otherParam = currentMethod.getParameter(variableId.getText());
            final VariableInfo otherVariable = currentMethod.getLocal(variableId.getText());

            if (otherVariable == null && otherParam == null) {
                // Add new VariableInfo for variable.
                currentMethod.addLocal(new VariableInfo(
                        variableId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
                // Redeclaration: Add variable as Undefined.
                currentMethod.addLocal(VariableInfo.Undefined);
                error("duplicate variable `%s`", line, column, variableId.getText());
            }
        }

        @Override
        public void inABlockStatement(final ABlockStatement block) {
            // Enter new block within current method.
            currentMethod.enterBlock();
        }

        @Override
        public void outABlockStatement(final ABlockStatement block) {
            // Leave block within current method.
            currentMethod.leaveBlock();
        }

        /**
         * Helper method to resolve an AST type to a Type.
         *
         * @param abstractType The input AST type.
         * @return The corresponding Type.
         */
        private Type resolve(PType abstractType) {
            if (abstractType instanceof AClassType) {
                // AST type is a class type.
                final AClassType type = (AClassType)abstractType;
                final TIdentifier id = type.getName();
                final ClassInfo classInfo = symbolTable.getClassInfo(id.getText());

                if (classInfo != null) {
                    // Class information found: Return its type.
                    return classInfo.getType();
                } else {
                    // Undeclared class: Return the Undefined type.
                    final int line = id.getLine();
                    final int column = id.getPos();
                    error("undeclared identifier `%s`", line, column, id.getText());
                    return UndefinedType.Instance;
                }
            } else {
                // AST type is a built-in type: Let BuiltInType handle resolution.
                return BuiltInType.fromAbstract(abstractType);
            }
        }

    } // End SecondPass
}
