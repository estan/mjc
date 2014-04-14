package mjc.analysis;

import java.util.ArrayList;
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
import mjc.node.Node;
import mjc.node.PType;
import mjc.node.TIdentifier;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.types.BuiltInType;
import mjc.types.ClassType;
import mjc.types.Type;
import mjc.types.UndefinedType;
import mjc.types.UnsupportedType;
import mjc.error.MiniJavaError;
import static mjc.error.MiniJavaErrorType.*;

/**
 * Symbol table builder.
 *
 * A SymbolTableBuilder builds a symbol table from the AST. It does so in two passes.
 * In the first pass, information about declared classes are added. In the second pass
 * the builder goes deeper and adds information about declared fields, methods and
 * variables.
 *
 * Errors are collected during the building process, and may be queried for using
 * {@link #hasErrors()} and {@link #getErrors()}.
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

    private final List<MiniJavaError> errors = new ArrayList<>();

    /**
     * Builds and returns a symbol table from the given AST.
     *
     * If errors occurred, they may be queried for using {@link #hasErrors()} and
     * {@link #getErrors()}.
     *
     * @param ast The input AST.
     * @return A symbol table constructed from the AST.
     */
    public SymbolTable build(final Node ast) {
        errors.clear();

        SymbolTable symbolTable = new SymbolTable();

        // Run the two passes to construct table.
        ast.apply(new FirstPass(symbolTable));
        ast.apply(new SecondPass(symbolTable));

        return symbolTable;
    }

    /**
     * @return true if errors occurred during symbol table construction.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * @return The list of errors collected during symbol table construction.
     */
    public List<MiniJavaError> getErrors() {
        return errors;
    }

    /**
     * Adds an encountered error to the list of errors.
     */
    private void error(final MiniJavaError error) {
        errors.add(error);
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
                name = name + UUID.randomUUID();
                classId.replaceBy(new TIdentifier(name, line, column));
                error(DUPLICATE_CLASS.on(line, column, classId.getText()));
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

            final String methodId = declaration.getMethodName().getText();
            final int line = declaration.getMethodName().getLine();
            final int column = declaration.getMethodName().getPos();

            // Check that main method name is "main".
            if (!methodId.equals("main")) {
                error(MISSING_MAIN.on(line, column, currentClass.getName()));
            }

            // Create MethodInfo for main method.
            final MethodInfo methodInfo = new MethodInfo(
                    methodId,
                    UnsupportedType.Void,
                    line, column);

            // Create VariableInfo for main method parameter.
            final TIdentifier paramId = declaration.getMethodParameter();
            final int paramLine = paramId.getLine();
            final int paramColumn = paramId.getPos();
            methodInfo.addParameter(new VariableInfo(
                    paramId.getText(),
                    UnsupportedType.StringArray,
                    paramLine, paramColumn));

            currentMethod = currentClass.addMethod(methodInfo.getName(), methodInfo);
            currentMethod.enterBlock();
        }

        @Override
        public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
            currentMethod.leaveBlock();
            currentMethod = null;
            currentClass = null;
        }

        @Override
        public void inAClassDeclaration(final AClassDeclaration declaration) {
            currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        }

        @Override
        public void outAClassDeclaration(final AClassDeclaration declaration) {
            currentClass = null;
        }

        @Override
        public void inAMethodDeclaration(final AMethodDeclaration declaration) {
            currentMethod = currentClass.getMethod(declaration.getName().getText());

            final TIdentifier methodId = declaration.getName();
            final int line = methodId.getLine();
            final int column = methodId.getPos();

            String name = methodId.getText();
            if (currentClass.getMethod(name) != null) {
                // Redeclaration: Use another name, and update AST to match.
                name = name + UUID.randomUUID();
                methodId.replaceBy(new TIdentifier(name, line, column));
                error(DUPLICATE_METHOD.on(line, column, methodId.getText()));
            }

            currentMethod = currentClass.addMethod(name,
                    new MethodInfo(methodId.getText(),
                            fromAbstract(declaration.getReturnType()),
                            line, column)
            );

            currentMethod.enterBlock();
        }

        @Override
        public void outAMethodDeclaration(final AMethodDeclaration declaration) {
            currentMethod.leaveBlock();
            currentMethod = null;
        }

        @Override
        public void inAFieldDeclaration(final AFieldDeclaration declaration) {
            final TIdentifier fieldId = declaration.getName();
            final int line = fieldId.getLine();
            final int column = fieldId.getPos();

            if (currentClass.getField(fieldId.getText()) == null) {
                currentClass.addField(new VariableInfo(
                        fieldId.getText(),
                        fromAbstract(declaration.getType()),
                        line, column));
            } else {
                error(DUPLICATE_FIELD.on(line, column, fieldId.getText()));
            }
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
                        fromAbstract(declaration.getType()),
                        line, column));
            } else {
                error(DUPLICATE_PARAMETER.on(line, column, paramId.getText()));
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
                        fromAbstract(declaration.getType()),
                        line, column));
            } else {
                error(DUPLICATE_VARIABLE.on(line, column, variableId.getText()));
            }
        }

        /**
         * Helper method: Returns the Type corresponding to an AST type.
         *
         * If @a abstractType is of class type, but the class is not declared, this method
         * returns UndefinedType.Instance and adds an error to the error list.
         *
         * @param abstractType Input AST type.
         * @return The corresponding Type, or UndefinedType.Instance if it is undeclared.
         * @throws RuntimeException if @a abstractType is of unknown PType subclass.
         */
        private Type fromAbstract(final PType abstractType) {
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
                    error(UNDECLARED_CLASS.on(line, column, classId.getText()));
                    return UndefinedType.Instance;
                }
            } else if (abstractType instanceof AIntType) {
                return BuiltInType.Int;
            } else if (abstractType instanceof AIntArrayType) {
                return BuiltInType.IntArray;
            } else if (abstractType instanceof ALongType) {
                return BuiltInType.Long;
            } else if (abstractType instanceof ALongArrayType) {
                return BuiltInType.LongArray;
            } else if (abstractType instanceof ABooleanType) {
                return BuiltInType.Boolean;
            } else {
                error(INTERNAL_ERROR.on(0, 0, "Unknown PType"));
                return UndefinedType.Instance;
            }
        }

    } // End SecondPass

}
