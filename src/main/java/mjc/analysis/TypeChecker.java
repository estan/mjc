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
import mjc.error.MiniJavaError;

import static mjc.error.MiniJavaErrorType.*;

/**
 * Type checker.
 *
 * The TypeChecker class takes as input an abstract syntax tree and a symbol table and
 * performs type-checking of the program. Errors are collected during the checking and
 * may be queried for using the {@link #hasErrors() hasErrors} and {@link #getErrors()
 * getErrors} methods.
 *
 * If any errors exist after construction of the symbol table and type-checking has
 * completed, the compiler should abort and not proceed with later stages of compilation.
 */
public class TypeChecker extends DepthFirstAdapter {
    private SymbolTable symbolTable;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    private HashMap<Node, Type> types;
    private List<MiniJavaError> errors;

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
    public List<MiniJavaError> getErrors() {
        return errors;
    }

    /**
     * Adds an encountered error to the list of errors.
     */
    private void error(final MiniJavaError error) {
        errors.add(error);
    }

    public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMethodName().getText());
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
            error(INVALID_RETURN_TYPE.on(line, column, id, returnType));
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

        if (!conditionType.isBoolean() && !conditionType.isUndefined()) {
            error(INVALID_CONDITION_TYPE.on(line, column, conditionType));
        }
    }

    public void outAIfElseStatement(final AIfElseStatement statement) {
        final Type conditionType = types.get(statement.getCondition());
        final int line = statement.getIfKeyword().getLine();
        final int column = statement.getIfKeyword().getPos();

        if (!conditionType.isBoolean() && !conditionType.isUndefined()) {
            error(INVALID_CONDITION_TYPE.on(line, column, conditionType));
        }
    }

    public void outAWhileStatement(final AWhileStatement statement) {
        final Type conditionType = types.get(statement.getCondition());
        final int line = statement.getWhileKeyword().getLine();
        final int column = statement.getWhileKeyword().getPos();

        if (!conditionType.isBoolean() && !conditionType.isUndefined()) {
            error(INVALID_CONDITION_TYPE.on(line, column, conditionType));
        }
    }

    public void outAPrintlnStatement(final APrintlnStatement statement) {
        final Type valueType = types.get(statement.getValue());
        final int line = statement.getPrintlnKeyword().getLine();
        final int column = statement.getPrintlnKeyword().getPos();

        if (!valueType.isInteger() && !valueType.isUndefined()) {
            error(INVALID_PRINTLN_TYPE.on(line, column, valueType));
        }
    }

    public void outAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getName().getText();
        final int line = statement.getAssign().getLine();
        final int column = statement.getAssign().getPos();

        Type left = null;
        final VariableInfo localInfo, paramInfo, fieldInfo;
        if ((localInfo = currentMethod.getLocal(id)) != null) {
            left = localInfo.getType();
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            left = paramInfo.getType();
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            left = fieldInfo.getType();
        }

        if (left == null) {
            if (symbolTable.getClassInfo(id) != null) {
                error(EXPECTED_VARIABLE_GOT_CLASS.on(line, column, id));
            } else {
                error(UNDECLARED_IDENTIFIER.on(line, column, id));
            }
        } else {
            final Type right = types.get(statement.getValue());
            if (!right.isAssignableTo(left)) {
                error(INVALID_ASSIGNMENT.on(line, column, right, left));
            }
        }
    }

    public void outAArrayAssignStatement(final AArrayAssignStatement statement) {
        final String id = statement.getName().getText();
        final int line = statement.getAssign().getLine();
        final int column = statement.getAssign().getPos();

        final Type indexType = types.get(statement.getIndex());
        if (!indexType.isInt() && !indexType.isUndefined()) {
            error(INVALID_INDEX_TYPE.on(line, column, indexType));
        }

        Type left = null;
        final VariableInfo localInfo, paramInfo, fieldInfo;
        if ((localInfo = currentMethod.getLocal(id)) != null) {
            left = localInfo.getType();
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            left = paramInfo.getType();
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            left = fieldInfo.getType();
        } else if (symbolTable.getClassInfo(id) != null) {
            error(EXPECTED_VARIABLE_GOT_CLASS.on(line, column, id));
        } else {
            error(UNDECLARED_IDENTIFIER.on(line, column, id));
        }

        if (left != null) {
            final Type right = types.get(statement.getValue());
            if (left.isIntArray()) {
                if (!right.isAssignableTo(BuiltInType.Int)) {
                    error(INVALID_ASSIGNMENT.on(line, column, right, BuiltInType.Int));
                }
            } else if (left.isLongArray()) {
                if (!right.isAssignableTo(BuiltInType.Long)) {
                    error(INVALID_ASSIGNMENT.on(line, column, right, BuiltInType.Long));
                }
            } else {
                error(NOT_ARRAY_TYPE.on(line, column, left));
            }
        }
    }

    public void outAAndExpression(final AAndExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getAnd().getLine();
        final int column = expression.getAnd().getPos();

        if (!left.isConjunctableWith(right)) {
            error(INVALID_BINARY_OP.on(line, column, "&&", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAOrExpression(final AOrExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getOr().getLine();
        final int column = expression.getOr().getPos();

        if (!left.isDisjunctableWith(right)) {
            error(INVALID_BINARY_OP.on(line, column, "||", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outALessThanExpression(final ALessThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getLessThan().getLine();
        final int column = expression.getLessThan().getPos();

        if (!left.isRelationalComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, "<", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAGreaterThanExpression(final AGreaterThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getGreaterThan().getLine();
        final int column = expression.getGreaterThan().getPos();

        if (!left.isRelationalComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, ">", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getGreaterEqualThan().getLine();
        final int column = expression.getGreaterEqualThan().getPos();

        if (!left.isRelationalComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, ">=", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outALessEqualThanExpression(final ALessEqualThanExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getLessEqualThan().getLine();
        final int column = expression.getLessEqualThan().getPos();

        if (!left.isRelationalComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, "<=", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAEqualExpression(final AEqualExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getEqual().getLine();
        final int column = expression.getEqual().getPos();

        if (!left.isEqualComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, "==", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outANotEqualExpression(final ANotEqualExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getNotEqual().getLine();
        final int column = expression.getNotEqual().getPos();

        if (!left.isEqualComparableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, "!=", left, right));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAPlusExpression(final APlusExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getPlus().getLine();
        final int column = expression.getPlus().getPos();

        if (!left.isAddableTo(right)) {
            error(INVALID_BINARY_OP.on(line, column, "+", left, right));
        }

        if (left.isLong() || right.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Int);
        }
    }

    public void outAMinusExpression(final AMinusExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getMinus().getLine();
        final int column = expression.getMinus().getPos();

        if (!right.isSubtractableFrom(left)) {
            error(INVALID_BINARY_OP.on(line, column, "-", left, right));
        }

        if (left.isLong() || right.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Int);
        }
    }

    public void outATimesExpression(final ATimesExpression expression) {
        final Type left = types.get(expression.getLeft());
        final Type right = types.get(expression.getRight());
        final int line = expression.getStar().getLine();
        final int column = expression.getStar().getPos();

        if (!left.isMultipliableWith(right)) {
            error(INVALID_BINARY_OP.on(line, column, "*", left, right));
        }

        if (left.isLong() || right.isLong()) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, BuiltInType.Int);
        }
    }

    public void outANotExpression(final ANotExpression expression) {
        final Type type = types.get(expression.getExpression());

        if (!type.isBoolean() && !type.isUndefined()) {
            final int line = expression.getNot().getLine();
            final int column = expression.getNot().getPos();
            error(INVALID_UNARY_OP.on(line, column, "!", type));
        }

        types.put(expression, BuiltInType.Boolean);
    }

    public void outAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        final Type classType = types.get(expression.getInstance());
        final String classId = classType.getName();

        final String methodId = expression.getName().getText();
        final MethodInfo methodInfo;

        final int line = expression.getName().getLine();
        final int column = expression.getName().getPos();

        if (!classType.isClass()) {
            if (!classType.isUndefined()) {
                error(CALL_ON_NON_CLASS.on(line, column, classType));
            }
            types.put(expression, UndefinedType.Instance);
        } else if ((methodInfo = symbolTable.getClassInfo(classId).getMethod(methodId)) == null) {
            error(UNDECLARED_METHOD.on(line, column, methodId));
            types.put(expression, UndefinedType.Instance);
        } else if (methodInfo == symbolTable.getMainMethod()) {
            error(CALL_TO_MAIN.on(line, column));
            types.put(expression, UndefinedType.Instance);
        } else {
            final List<PExpression> actuals = expression.getActuals();
            final List<VariableInfo> formals = methodInfo.getParameters();
            if (actuals.size() != formals.size()) {
                error(INVALID_PARAM_COUNT.on(line, column, methodId, actuals.size(), formals.size()));
            } else {
                final Iterator<PExpression> actualsIt = actuals.iterator();
                final Iterator<VariableInfo> formalsIt = formals.iterator();
                int paramNr = 0;
                while (actualsIt.hasNext()) {
                    final Type actual = types.get(actualsIt.next());
                    final Type formal = formalsIt.next().getType();
                    if (!actual.isAssignableTo(formal)) {
                        error(INVALID_PARAM_TYPE.on(line, column, methodId, paramNr, actual, formal));
                    }
                    ++paramNr;
                }
            }
            types.put(expression, methodInfo.getReturnType());
        }
    }

    public void outAArrayAccessExpression(final AArrayAccessExpression expression) {
        final Type type = types.get(expression.getArray());
        final Type indexType = types.get(expression.getIndex());
        final int line = expression.getStartBracket().getLine();
        final int column = expression.getStartBracket().getPos();

        if (!indexType.isInt() && !indexType.isUndefined()) {
            error(INVALID_INDEX_TYPE.on(line, column, indexType));
        }

        if (type.isIntArray()) {
            types.put(expression, BuiltInType.Int);
        } else if (type.isLongArray()) {
            types.put(expression, BuiltInType.Long);
        } else {
            if (!type.isUndefined()) {
                error(NOT_ARRAY_TYPE.on(line, column, type));
            }
            types.put(expression, BuiltInType.Int);
        }
    }

    public void outAArrayLengthExpression(final AArrayLengthExpression expression) {
        final Type type = types.get(expression.getArray());

        if (!type.isArray() && !type.isUndefined()) {
            final int line = expression.getLengthKeyword().getLine();
            final int column = expression.getLengthKeyword().getPos();
            error(LENGTH_ON_NON_ARRAY.on(line, column, type));
        }

        types.put(expression, BuiltInType.Int);
    }

    public void outANewInstanceExpression(final ANewInstanceExpression expression) {
        final TIdentifier id = expression.getClassName();
        final ClassInfo classInfo = symbolTable.getClassInfo(id.getText());

        if (classInfo != null) {
            types.put(expression, classInfo.getType());
        } else {
            final int line = id.getLine();
            final int column = id.getPos();
            error(UNDECLARED_CLASS.on(line, column, id.getText()));
            types.put(expression, UndefinedType.Instance);
        }
    }

    public void outANewIntArrayExpression(final ANewIntArrayExpression expression) {
        final Type type = types.get(expression.getSize());

        if (!type.isInt() && !type.isUndefined()) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(INVALID_SIZE_TYPE.on(line, column, type));
        }

        types.put(expression, BuiltInType.IntArray);
    }

    public void outANewLongArrayExpression(final ANewLongArrayExpression expression) {
        final Type type = types.get(expression.getSize());

        if (!type.isInt() && !type.isUndefined()) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(INVALID_SIZE_TYPE.on(line, column, type));
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
            error(INVALID_INT_LITERAL.on(line, column, literal));
        }

        types.put(expression, BuiltInType.Int);
    }

    public void outALongExpression(final ALongExpression expression) {
        final String literal = expression.getLong().getText();

        try {
            Long.parseLong(literal.substring(0, literal.length() - 1)); // Strip 'L'/'l'.
        } catch (NumberFormatException e) {
            final int line = expression.getLong().getLine();
            final int column = expression.getLong().getPos();
            error(INVALID_LONG_LITERAL.on(line, column, literal));
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
            error(EXPECTED_VARIABLE_GOT_CLASS.on(line, column, id));
            types.put(expression, UndefinedType.Instance);
        } else {
            error(UNDECLARED_IDENTIFIER.on(line, column, id));
            types.put(expression, UndefinedType.Instance);
        }
    }

    public void outAThisExpression(final AThisExpression expression) {
        types.put(expression, currentClass.getType());
    }
}
