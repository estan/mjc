package mjc.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mjc.node.AAndExpression;
import mjc.node.AArrayAccessExpression;
import mjc.node.AArrayAssignStatement;
import mjc.node.AArrayLengthExpression;
import mjc.node.AAssignStatement;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AEqualExpression;
import mjc.node.AFalseExpression;
import mjc.node.AGreaterEqualThanExpression;
import mjc.node.AGreaterThanExpression;
import mjc.node.AIdentifierExpression;
import mjc.node.AIfElseStatement;
import mjc.node.AIfStatement;
import mjc.node.AIntegerExpression;
import mjc.node.ALessEqualThanExpression;
import mjc.node.ALessThanExpression;
import mjc.node.ALongExpression;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AMethodInvocationExpression;
import mjc.node.AMinusExpression;
import mjc.node.ANewInstanceExpression;
import mjc.node.ANewIntArrayExpression;
import mjc.node.ANewLongArrayExpression;
import mjc.node.ANotEqualExpression;
import mjc.node.ANotExpression;
import mjc.node.AOrExpression;
import mjc.node.APlusExpression;
import mjc.node.APrintlnStatement;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
import mjc.node.AWhileStatement;
import mjc.node.Node;
import mjc.node.PExpression;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.types.BuiltInType;
import mjc.types.Type;
import mjc.types.UndefinedType;

/**
 * Type checker.
 *
 * The TypeChecker class takes as input an abstract syntax tree and a symbol table and
 * performs type-checking of the program. Errors are collected during the checking and
 * may be queried for using the {@link #hasErrors() hasErrors} and {@link #getErrors()
 * getErrors} methods.
 *
 * The compiler should not proceed if errors occurred during construction of the symbol
 * table or during type-checking.
 */
public class TypeChecker extends DepthFirstAdapter {
    private SymbolTable symbolTable;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    private HashMap<Node, Type> types;
    private List<String> errors;

    /**
     * Perform type-checking on the given tree using the given symbol table.
     *
     * @param tree Input abstract syntax tree.
     * @param symbolTable Symbol table for the tree.
     * @return true if checking completed without errors, otherwise false.
     */
    public boolean check(final Start tree, final SymbolTable symbolTable) {
        this.symbolTable = symbolTable;

        types = new HashMap<>();
        errors = new ArrayList<>();

        // Apply the type-checker.
        tree.apply(this);

        return !hasErrors();
    }

    /**
     * @return true if errors occurred during type-checking.
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * @return The list of errors collected during type-checking.
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

    public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMainMethodName().getText());
        currentMethod.enterBlock();
    }

    public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentMethod.leaveBlock();
    }

    public void inAClassDeclaration(final AClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
    }

    public void inAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();
    }

    public void outAMethodDeclaration(final AMethodDeclaration declaration) {
        final String id = declaration.getName().getText();
        final Type actualType = types.get(declaration.getReturnExpression());
        final Type returnType = currentMethod.getReturnType();
        final int line = declaration.getName().getLine();
        final int column = declaration.getName().getPos();

        if (!actualType.isAssignableTo(returnType)) {
            error(line, column, "method `%s` must return value of type %s", id, returnType);
        }

        currentMethod.leaveBlock();

    }

    public void inABlockStatement(final ABlockStatement block) {
        currentMethod.enterBlock();
    }

    public void outABlockStatement(final ABlockStatement block) {
        currentMethod.leaveBlock();
    }

    public void outAIfStatement(final AIfStatement statement) {
        final Type conditionType = types.get(statement.getCondition());
        final int line = statement.getIfKeyword().getLine();
        final int column = statement.getIfKeyword().getPos();

        if (!(conditionType.isBoolean() || conditionType.isUndefined())) {
            error(line, column, "if: condition must be of type boolean");
        }
    }

    public void outAIfElseStatement(final AIfElseStatement statement) {
        final Type conditionType = types.get(statement.getCondition());
        final int line = statement.getIfKeyword().getLine();
        final int column = statement.getIfKeyword().getPos();

        if (!(conditionType.isBoolean() || conditionType.isUndefined())) {
            error(line, column, "if/else: condition must be of type boolean");
        }
    }

    public void outAWhileStatement(final AWhileStatement statement) {
        final Type conditionType = types.get(statement.getCondition());
        final int line = statement.getWhileKeyword().getLine();
        final int column = statement.getWhileKeyword().getPos();

        if (!(conditionType.isBoolean() || conditionType.isUndefined())) {
            error(line, column, "while: condition must be of type boolean");
        }
    }

    public void outAPrintlnStatement(final APrintlnStatement statement) {
        final Type valueType = types.get(statement.getValue());
        final int line = statement.getPrintlnKeyword().getLine();
        final int column = statement.getPrintlnKeyword().getPos();

        if (!(valueType.isInteger() || valueType.isUndefined())) {
            error(line, column, "can't print value of type %s", valueType);
        }
    }

    public void outAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getVariable().getText();
        final int line = statement.getAssign().getLine();
        final int column = statement.getAssign().getPos();

        Type type = null;
        final VariableInfo localInfo, paramInfo, fieldInfo;
        if ((localInfo = currentMethod.getLocal(id)) != null) {
            type = localInfo.getType();
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            type = paramInfo.getType();
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            type = fieldInfo.getType();
        } else if (symbolTable.getClassInfo(id) != null) {
            error(line, column, "`%s` is a class name, expected variable name", id);
        } else {
            error(line, column, "undeclared identifier `%s`", id);
        }

        if (type != null) {
            final Type valueType = types.get(statement.getValue());
            if (!valueType.isAssignableTo(type)) {
                error(line, column, "can't assign %s to %s", valueType, type);
            }
        }
    }

    public void outAArrayAssignStatement(final AArrayAssignStatement statement) {
        final String id = statement.getVariable().getText();
        final int line = statement.getAssign().getLine();
        final int column = statement.getAssign().getPos();

        final Type indexType = types.get(statement.getIndex());
        if (!(indexType.isInt() || indexType.isUndefined())) {
            error(line, column, "index of type %s, expected int", indexType);
        }

        Type type = null;
        final VariableInfo localInfo, paramInfo, fieldInfo;
        if ((localInfo = currentMethod.getLocal(id)) != null) {
            type = localInfo.getType();
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            type = paramInfo.getType();
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            type = fieldInfo.getType();
        } else if (symbolTable.getClassInfo(id) != null) {
            error(line, column, "`%s` is a class name, expected variable name", id);
        } else {
            error(line, column, "undeclared identifier `%s`", id);
        }

        if (type != null) {
            final Type valueType = types.get(statement.getValue());
            if (type.isIntArray()) {
                if (!(valueType.isInt() || valueType.isUndefined())) {
                    error(line, column, "can't assign %s to int", valueType);
                }
            } else if (type.isLongArray()) {
                if (!(valueType.isInteger() || valueType.isUndefined())) {
                    error(line, column, "can't assign %s to long", valueType);
                }
            } else {
                error(line, column, "`%s` is of non-array type %s", id, type);
            }
        }
    }

    public void outAAndExpression(final AAndExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getAnd().getLine();
        final int column = expression.getAnd().getPos();

        if (!(left.isBoolean() || left.isUndefined())) {
            error(line, column, "&&: unsupported left-hand type %s", left);
        }

        if (!(right.isBoolean() || right.isUndefined())) {
            error(line, column, "&&: unsupported right-hand type %s", right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAOrExpression(final AOrExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getOr().getLine();
        final int column = expression.getOr().getPos();

        if (!(left.isBoolean() || left.isUndefined())) {
            error(line, column, "||: unsupported left-hand type %s", left);
        }

        if (!(right.isBoolean() || right.isUndefined())) {
            error(line, column, "||: unsupported right-hand type %s", right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outALessThanExpression(final ALessThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getLessThan().getLine();
        final int column = expression.getLessThan().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, "<: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAGreaterThanExpression(final AGreaterThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getGreaterThan().getLine();
        final int column = expression.getGreaterThan().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, ">: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getGreaterEqualThan().getLine();
        final int column = expression.getGreaterEqualThan().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, ">=: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outALessEqualThanExpression(final ALessEqualThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getLessEqualThan().getLine();
        final int column = expression.getLessEqualThan().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, "<=: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAEqualExpression(final AEqualExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getEqual().getLine();
        final int column = expression.getEqual().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isClass() && right.isClass() ||
                left.isBoolean() && right.isBoolean() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, "==: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outANotEqualExpression(final ANotEqualExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getNotEqual().getLine();
        final int column = expression.getNotEqual().getPos();

        if (!(left.isInteger() && right.isInteger() ||
                left.isClass() && right.isClass() ||
                left.isBoolean() && right.isBoolean() ||
                left.isUndefined() || right.isUndefined())) {
            error(line, column, "!=: can't compare %s to %s", left, right);
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAPlusExpression(final APlusExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getPlus().getLine();
        final int column = expression.getPlus().getPos();

        if (!(left.isInteger() || left.isUndefined())) {
            error(line, column, "+: unsupported left-hand type %s", left);
        }

        if (!(right.isInteger() || right.isUndefined())) {
            error(line, column, "+: unsupported right-hand type %s", right);
        }

        if (left.isLong() || right.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Integer); // Possibly a guess.
        }
    }

    public void outAMinusExpression(final AMinusExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getMinus().getLine();
        final int column = expression.getMinus().getPos();

        if (!(left.isInteger() || left.isUndefined())) {
            error(line, column, "-: unsupported left-hand type %s", left);
        }

        if (!(right.isInteger() || right.isUndefined())) {
            error(line, column, "-: unsupported right-hand type %s", right);
        }

        if (left.isInt() && right.isLong()) {
            error(line, column, "-: can't subtract long from int", left);
        }

        if (left.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Integer); // Possibly a guess.
        }
    }

    public void outATimesExpression(final ATimesExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getStar().getLine();
        final int column = expression.getStar().getPos();

        if (!(right.isInteger() || right.isUndefined())) {
            error(line, column, "*: unsupported right-hand type %s", right);
        }

        if (!(left.isInteger() || left.isUndefined())) {
            error(line, column, "*: unsupported left-hand type %s", left);
        }

        if (left.isLong() || right.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Integer); // Possibly a guess.
        }
    }

    public void outANotExpression(final ANotExpression expression) {
        types.put(expression, BuiltInType.Boolean);
        final Type type = types.get(expression.getExpression());
        if (!(type.isBoolean() || type.isUndefined())) {
            final int line = expression.getNot().getLine();
            final int column = expression.getNot().getPos();
            error(line, column, "!: expected boolean expression, but got %s", type);
        }
    }

    public void outAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        final Type type = types.get(expression.getInstance());
        final String methodId = expression.getName().getText();
        final int line = expression.getName().getLine();
        final int column = expression.getName().getPos();

        if (type.isClass()) {
            final ClassInfo classInfo = symbolTable.getClassInfo(type.getName());
            final MethodInfo methodInfo = classInfo.getMethod(methodId);

            if (methodInfo != null) {
                // Check if actual parameters match formal parameters.
                final List<PExpression> actuals = expression.getActualParameters();
                final List<VariableInfo> formals = methodInfo.getParameters();
                if (actuals.size() == formals.size()) {
                    final Iterator<PExpression> actualsIt = actuals.iterator();
                    final Iterator<VariableInfo> formalsIt = formals.iterator();
                    int param = 0;
                    while (actualsIt.hasNext()) {
                        final Type actualType = types.get(actualsIt.next());
                        final Type formalType = formalsIt.next().getType();
                        if (!actualType.isAssignableTo(formalType)) {
                            error(line, column,
                                    "in call to `%s`: parameter %d of type %s, expected %s",
                                    methodId, param, actualType, formalType);
                        }
                        ++param;
                    }

                } else {
                    error(line, column,
                            "in call to `%s`: %d parameters given, expected %d",
                            methodId, actuals.size(), formals.size());
                }
                types.put(expression, methodInfo.getReturnType());
            } else {
                error(line, column, "no method `%s` in class `%s`", methodId, classInfo.getName());
                types.put(expression, UndefinedType.Instance);
            }
        } else {
            if (!type.isUndefined()) {
                error(line, column, "method call on expression of non-class type %s", type);
            }
            types.put(expression, UndefinedType.Instance);
        }
    }

    public void outAArrayAccessExpression(final AArrayAccessExpression expression) {
        final Type type = types.get(expression.getArray());
        final Type indexType = types.get(expression.getIndex());
        final int line = expression.getStartBracket().getLine();
        final int column = expression.getStartBracket().getPos();

        if (!(indexType.isInt() || indexType.isUndefined())) {
            error(line, column, "index of type %s, expected int", indexType);
        }

        if (type.isIntArray()) {
            types.put(expression, BuiltInType.Integer);
        } else if (type.isLongArray()) {
            types.put(expression, BuiltInType.Long);
        } else {
            if (!type.isUndefined()) {
                error(line, column, "%s is not an array type", type);
            }
            types.put(expression, BuiltInType.Integer); // Guess int[].
        }
    }

    public void outAArrayLengthExpression(final AArrayLengthExpression expression) {
        final Type type = types.get(expression.getArray());
        if (!(type.isArray() || type.isUndefined())) {
            final int line = expression.getLengthKeyword().getLine();
            final int column = expression.getLengthKeyword().getPos();
            error(line, column, "length: unsupported on non-array type %s", type);
        }
        types.put(expression, BuiltInType.Integer);
    }

    public void outANewInstanceExpression(final ANewInstanceExpression expression) {
        final TIdentifier id = expression.getClassName();
        final ClassInfo classInfo = symbolTable.getClassInfo(id.getText());

        if (classInfo != null) {
            types.put(expression, classInfo.getType());
        } else {
            final int line = id.getLine();
            final int column = id.getPos();
            error(line, column, "undeclared class `%s`", id.getText());
            types.put(expression, UndefinedType.Instance);
        }
    }

    public void outANewIntArrayExpression(final ANewIntArrayExpression expression) {
        types.put(expression, BuiltInType.IntegerArray);
        final Type type = types.get(expression.getSize());
        if (!(type.isInt() || type.isUndefined())) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(line, column, "array size expression of type %s, expected int", type);
        }
    }

    public void outANewLongArrayExpression(final ANewLongArrayExpression expression) {
        final Type type = types.get(expression.getSize());
        if (!(type.isInt() || type.isUndefined())) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(line, column, "array size expression of type %s, expected int", type);
        }
        types.put(expression, BuiltInType.LongArray);
    }

    public void outAIntegerExpression(final AIntegerExpression expression) {
        final String literal = expression.getInteger().getText();
        try {
            Integer.parseInt(literal);
        } catch (NumberFormatException e) {
            final int line = expression.getInteger().getLine();
            final int column = expression.getInteger().getPos();
            error(line, column, "invalid int literal %s", literal);
        }
        types.put(expression, BuiltInType.Integer);
    }

    public void outALongExpression(final ALongExpression expression) {
        final String literal = expression.getLong().getText();
        try {
            Long.parseLong(literal.substring(0, literal.length() - 1));
        } catch (NumberFormatException e) {
            final int line = expression.getLong().getLine();
            final int column = expression.getLong().getPos();
            error(line, column, "invalid long literal %s", literal);
        }
        types.put(expression, BuiltInType.Long);
    }

    public void outATrueExpression(final ATrueExpression expression) {
        types.put(expression, BuiltInType.Boolean);
    }

    public void outAFalseExpression(final AFalseExpression expression) {
        types.put(expression, BuiltInType.Boolean);
    }

    public void outAIdentifierExpression(final AIdentifierExpression expression) {
        final String id = expression.getIdentifier().getText();
        final int line = expression.getIdentifier().getLine();
        final int column = expression.getIdentifier().getPos();

        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            types.put(expression, localInfo.getType());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            types.put(expression, paramInfo.getType());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            types.put(expression, fieldInfo.getType());
        } else if (symbolTable.getClassInfo(id) != null) {
            error(line, column, "`%s` is a class name, expected variable name", id);
            types.put(expression, UndefinedType.Instance);
        } else {
            error(line, column, "undeclared identifier `%s`", id);
            types.put(expression, UndefinedType.Instance);
        }
    }

    public void outAThisExpression(final AThisExpression expression) {
        types.put(expression, currentClass.getType());
    }
}