package mjc.symbol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.ABooleanType;
import mjc.node.AClassDeclaration;
import mjc.node.AClassType;
import mjc.node.AFieldDeclaration;
import mjc.node.AFormalParameter;
import mjc.node.AIntArrayType;
import mjc.node.AIntType;
import mjc.node.ALongArrayType;
import mjc.node.ALongType;
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
 * {@link #hasErrors() hasErrors} and {@link #getErrors() getErrors}.
 *
 * It is OK to proceed with type checking even if errors occurred during symbol table
 * construction: The constructed table is still complete, but:
 *
 *   - Fields, parameters, local variables and methods with undeclared types / return
 *     types will have been entered with the UndefinedType type.
 *
 *   - Redeclared methods and classes will have been entered under new unique name
 *     and the AST will have been updated to match.
 */
public class SymbolTableBuilder {

    private final List<String> errors = new ArrayList<>();

    /**
     * Builds and returns a symbol table from the given AST.
     *
     * If errors occurred, they may be queried for using {@link #hasErrors() hasErrors}
     * and {@link #getErrors() getErrors}.
     *
     * @param tree The input AST.
     * @return A symbol table constructed from the AST.
     */
    public SymbolTable build(final Start tree) {
        errors.clear();

        SymbolTable symbolTable = new SymbolTable();

        // Run the two passes to construct table.
        tree.apply(new FirstPass(symbolTable));
        tree.apply(new SecondPass(symbolTable));

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
     * Visitor for first pass of symbol table construction.
     *
     * In the first pass, we only add information about declared classes.
     */
    private class FirstPass extends DepthFirstAdapter {

        private final SymbolTable symbolTable;

        /**
         * Constructs a new FirstPass.
         *
         * @param symbolTable Symbol table under construction.
         */
        public FirstPass(final SymbolTable symbolTable) {
            this.symbolTable = symbolTable;
        }

        @Override
        public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
            final TIdentifier classId = declaration.getName();
            final TIdentifier methodId = declaration.getMainMethodName();

            // Check that main method name is "main".
            if (!methodId.getText().equals("main")) {
                error("name of method in class `%s` must be \"main\"",
                        methodId.getLine(), methodId.getPos(), classId.getText());
            }

            symbolTable.addClassInfo(classId.getText(), new ClassInfo(
                    classId.getText(),
                    new ClassType(classId.getText()),
                    classId.getLine(), classId.getPos())
            );
        }

        @Override
        public void inAClassDeclaration(final AClassDeclaration declaration) {
            final TIdentifier classId = declaration.getName();
            final int line = classId.getLine();
            final int column = classId.getPos();

            String name = classId.getText();
            if (symbolTable.getClassInfo(name) != null) {
                // Redeclaration: Enter ClassInfo under new name, and update AST to match.
                name = name + "-REDECLARED-" + UUID.randomUUID();
                classId.replaceBy(new TIdentifier(name, line, column));
                error("duplicate class `%s`", line, column, classId.getText());
            }

            symbolTable.addClassInfo(name, new ClassInfo(
                    classId.getText(),
                    new ClassType(classId.getText()),
                    line, column)
            );
        }

    } // End FirstPass

    /**
     * Visitor for second pass of symbol table construction.
     *
     * In the second pass, we add information about declared fields, methods and
     * local variables.
     */
    private class SecondPass extends DepthFirstAdapter {

        private final SymbolTable symbolTable;
        private ClassInfo currentClass;
        private MethodInfo currentMethod;

        /**
         * Constructs a new SecondPass.
         *
         * @param symbolTable Symbol table under construction.
         */
        public SecondPass(final SymbolTable symbolTable) {
            this.symbolTable = symbolTable;
        }

        @Override
        public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
            currentClass = symbolTable.getClassInfo(declaration.getName().getText());

            // Create MethodInfo for main method.
            final TIdentifier methodId = declaration.getMainMethodName();
            final MethodInfo methodInfo = new MethodInfo(
                    methodId.getText(),
                    UnsupportedType.Void,
                    methodId.getLine(), methodId.getPos());

            // Create VariableInfo for main method parameter.
            final TIdentifier paramId = declaration.getArguments();
            methodInfo.addParameter(new VariableInfo(
                    paramId.getText(),
                    UnsupportedType.StringArray,
                    paramId.getLine(), paramId.getPos()));

            currentMethod = currentClass.addMethod(methodInfo.getName(), methodInfo);
            currentMethod.enterBlock();
        }

        @Override
        public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
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
                currentClass.addField(new VariableInfo(
                        fieldId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
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
            boolean isRedeclaration = false;
            if (otherInfo != null) { // Another method with the same name exists.
                final List<PFormalParameter> params = declaration.getFormalParameters();
                final List<VariableInfo> otherParams = otherInfo.getParameters();

                // If parameter lists are equal, this is a redeclaration.
                if (params.size() == otherParams.size()) {
                    Iterator<PFormalParameter> it = params.iterator();
                    Iterator<VariableInfo> otherIt = otherParams.iterator();

                    isRedeclaration = true;
                    while (it.hasNext()) {
                        AFormalParameter param = (AFormalParameter) it.next();
                        VariableInfo otherParam = otherIt.next();
                        if (!resolve(param.getType()).equals(otherParam.getType())) {
                            // Type of at least one parameter differs.
                            isRedeclaration = false;
                            break;
                        }
                    }
                }
            }

            String name = methodId.getText();
            if (isRedeclaration) {
                // Redeclaration: Use another name, and update AST to match.
                name = name + "-REDECLARED-" + UUID.randomUUID();
                methodId.replaceBy(new TIdentifier(name, line, column));
                error("duplicate method `%s`", line, column, methodId.getText());
            }

            currentMethod = currentClass.addMethod(name,
                    new MethodInfo(methodId.getText(),
                            resolve(declaration.getReturnType()),
                            line, column)
            );

            currentMethod.enterBlock();
        }

        @Override
        public void outAMethodDeclaration(final AMethodDeclaration declaration) {
            currentMethod.leaveBlock();
        }

        @Override
        public void inABlockStatement(final ABlockStatement block) {
            currentMethod.enterBlock();
        }

        @Override
        public void outABlockStatement(final ABlockStatement block) {
            currentMethod.leaveBlock();
        }

        @Override
        public void inAFormalParameter(final AFormalParameter declaration) {
            final TIdentifier paramId = declaration.getName();
            final int line = paramId.getLine();
            final int column = paramId.getPos();

            if (currentMethod.getParameter(paramId.getText()) == null) {
                currentMethod.addParameter(new VariableInfo(
                        paramId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
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
                currentMethod.addLocal(new VariableInfo(
                        variableId.getText(),
                        resolve(declaration.getType()),
                        line, column));
            } else {
                error("duplicate variable `%s`", line, column, variableId.getText());
            }
        }

        /**
         * Helper method to resolve an AST type to a Type.
         *
         * @param abstractType The input AST type.
         * @return The corresponding Type.
         * @throws RuntimeException if @a abstractType is an unknown PType.
         */
        private Type resolve(PType abstractType) {
            if (abstractType instanceof AClassType) {
                // AST type is a class type.
                final AClassType classType = (AClassType)abstractType;
                final TIdentifier classId = classType.getName();
                final ClassInfo classInfo = symbolTable.getClassInfo(classId.getText());

                if (classInfo != null) {
                    return classInfo.getType();
                } else {
                    final int line = classId.getLine();
                    final int column = classId.getPos();
                    error("undeclared identifier `%s`", line, column, classId.getText());
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

    } // End SecondPass

}