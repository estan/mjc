package mjc.symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AFieldDeclaration;
import mjc.node.AFormalParameter;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AVariableDeclaration;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.types.ClassType;
import mjc.types.Type;
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
     * @param line Line of the error.
     * @param column Column of the error.
     * @param format Error message as a formatted string referring to argument in @a args.
     * @param args Arguments referred to in @a format.
     */
    private void error(int line, int column, final String format, final Object... args) {
        errors.add("[" + line + "," + column + "] " + String.format(format, args));
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
                error(methodId.getLine(), methodId.getPos(), "missing main method in `%s`",
                    classId.getText());
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
                error(line, column, "duplicate class `%s`", classId.getText());
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
                        Type.fromAbstract(declaration.getType(), symbolTable, errors),
                        line, column));
            } else {
                error(line, column, "duplicate field `%s`", fieldId.getText());
            }
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
                name = name + "-REDECLARED-" + UUID.randomUUID();
                methodId.replaceBy(new TIdentifier(name, line, column));
                error(line, column, "duplicate method `%s`", methodId.getText());
            }

            currentMethod = currentClass.addMethod(name,
                    new MethodInfo(methodId.getText(),
                            Type.fromAbstract(declaration.getReturnType(), symbolTable, errors),
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
                        Type.fromAbstract(declaration.getType(), symbolTable, errors),
                        line, column));
            } else {
                error(line, column, "duplicate parameter `%s`", paramId.getText());
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
                        Type.fromAbstract(declaration.getType(), symbolTable, errors),
                        line, column));
            } else {
                error(line, column, "duplicate variable `%s`", variableId.getText());
            }
        }

    } // End SecondPass

}
