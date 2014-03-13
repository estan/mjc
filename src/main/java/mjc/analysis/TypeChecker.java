package mjc.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mjc.node.AArrayAccessExpression;
import mjc.node.AArrayLengthExpression;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AFalseExpression;
import mjc.node.AIdentifierExpression;
import mjc.node.AIntegerExpression;
import mjc.node.ALongExpression;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AMethodInvocationExpression;
import mjc.node.AMinusExpression;
import mjc.node.ANewInstanceExpression;
import mjc.node.ANewIntArrayExpression;
import mjc.node.ANewLongArrayExpression;
import mjc.node.ANotExpression;
import mjc.node.APlusExpression;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
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
        currentMethod.leaveBlock();
    }

    public void inABlockStatement(final ABlockStatement block) {
        currentMethod.enterBlock();
    }

    public void outABlockStatement(final ABlockStatement block) {
        currentMethod.leaveBlock();
    }

    public void outAPlusExpression(final APlusExpression expression) {
        final Type leftType = types.get(expression.getLeft());
        final Type rightType = types.get(expression.getRight());
        final int line = expression.getPlus().getLine();
        final int column = expression.getPlus().getPos();

        if (leftType == BuiltInType.Integer) {
            if (rightType == BuiltInType.Integer) {
                types.put(expression, BuiltInType.Integer);
            } else if (rightType == BuiltInType.Long) {
                types.put(expression, BuiltInType.Long);
            } else {
                types.put(expression, UndefinedType.Instance);
                error(line, column, "+: unsupported right-hand type %s", rightType);
            }
        } else if (leftType == BuiltInType.Long) {
            if (rightType == BuiltInType.Integer || rightType == BuiltInType.Long) {
                types.put(expression, BuiltInType.Long);
            } else {
                types.put(expression, UndefinedType.Instance);
                error(line, column, "+: unsupported right-hand type %s", rightType);
            }
        } else {
            types.put(expression, UndefinedType.Instance);
            error(line, column, "+: unsupported left-hand type %s", leftType);
        }
    }

    public void outAMinusExpression(final AMinusExpression expression) {
        final Type leftType = types.get(expression.getLeft());
        final Type rightType = types.get(expression.getRight());
        final int line = expression.getMinus().getLine();
        final int column = expression.getMinus().getPos();

        if (leftType == BuiltInType.Integer) {
            if (rightType == BuiltInType.Integer) {
                types.put(expression, BuiltInType.Integer);
            } else {
                types.put(expression, UndefinedType.Instance);
                error(line, column, "-: unsupported right-hand type %s", rightType);
            }
        } else if (leftType == BuiltInType.Long) {
            if (rightType == BuiltInType.Integer || rightType == BuiltInType.Long) {
                types.put(expression, BuiltInType.Long);
            } else {
                types.put(expression, UndefinedType.Instance);
                error(line, column, "-: unsupported right-hand type %s", rightType);
            }
        } else {
            types.put(expression, UndefinedType.Instance);
            error(line, column, "-: unsupported left-hand type %s", leftType);
        }
    }

    public void outATimesExpression(final ATimesExpression expression) {
        final Type leftType = types.get(expression.getLeft());
        final Type rightType = types.get(expression.getRight());
        final int line = expression.getStar().getLine();
        final int column = expression.getStar().getPos();

        if (leftType == BuiltInType.Long || leftType == BuiltInType.Integer) {
            if (rightType == BuiltInType.Long || rightType == BuiltInType.Integer) {
                if (leftType == BuiltInType.Long || rightType == BuiltInType.Long) {
                    types.put(expression, BuiltInType.Long);
                } else {
                    types.put(expression, BuiltInType.Integer);
                }
            } else {
                types.put(expression, UndefinedType.Instance);
                error(line, column, "*: unsupported right-hand type %s", rightType);
            }
        } else {
            types.put(expression, UndefinedType.Instance);
            error(line, column, "*: unsupported left-hand type %s", leftType);
        }
    }

    public void outANotExpression(final ANotExpression expression) {
        final Type type = types.get(expression.getExpression());
        if (type != BuiltInType.Boolean) {
            final int line = expression.getNot().getLine();
            final int column = expression.getNot().getPos();
            error(line, column, "!: expected boolean expression, but got %s", type);
        }
        types.put(expression, BuiltInType.Boolean);
    }

    public void outAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        final Type type = types.get(expression.getInstance());
        final String methodId = expression.getName().getText();
        final int line = expression.getName().getLine();
        final int column = expression.getName().getPos();

        if (type == UndefinedType.Instance) {
            types.put(expression, UndefinedType.Instance);
        } else if (type.isClass()) {
            final ClassInfo classInfo = symbolTable.getClassInfo(type.getName());
            final MethodInfo methodInfo = classInfo.getMethod(methodId);
            if (methodInfo != null) {
                // Check if parameters match.
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
                types.put(expression, UndefinedType.Instance);
                error(line, column, "undeclared method `%s`", methodId);
            }
        } else {
            types.put(expression, UndefinedType.Instance);
            error(line, column, "method call on expression of non-class type %s", type);
        }
    }

    public void outAArrayAccessExpression(final AArrayAccessExpression expression) {
        final Type type = types.get(expression.getArray());
        final Type indexType = types.get(expression.getIndex());
        final int line = expression.getStartBracket().getLine();
        final int column = expression.getStartBracket().getPos();

        if (indexType != BuiltInType.Integer) {
            error(line, column, "array index expression of type %s, expected int", indexType);
        }

        if (type == BuiltInType.IntegerArray) {
            types.put(expression, BuiltInType.Integer);
        } else if (type == BuiltInType.LongArray) {
            types.put(expression, BuiltInType.Long);
        } else {
            types.put(expression, UndefinedType.Instance);
            error(line, column, "accessed expression of type %s, expected array", type);
        }
    }

    public void outAArrayLengthExpression(final AArrayLengthExpression expression) {
        final Type type = types.get(expression.getArray());
        if (type != BuiltInType.IntegerArray && type != BuiltInType.LongArray) {
            final int line = expression.getLengthKeyword().getLine();
            final int column = expression.getLengthKeyword().getPos();
            error(line, column, "length unsupported on non-array type %s", type);
        }
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
        final Type type = types.get(expression.getSize());
        if (type != BuiltInType.Integer) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(line, column, "array size expression of type %s, expected int", type);
        }
        types.put(expression, BuiltInType.IntegerArray);
    }

    public void outANewLongArrayExpression(final ANewLongArrayExpression expression) {
        final Type type = types.get(expression.getSize());
        if (type != BuiltInType.Integer) {
            final int line = expression.getNewKeyword().getLine();
            final int column = expression.getNewKeyword().getPos();
            error(line, column, "array size expression of type %s, expected int", type);
        }
        types.put(expression, BuiltInType.LongArray);
    }

    public void outAIntegerExpression(final AIntegerExpression expression) {
        types.put(expression, BuiltInType.Integer);
    }

    public void outALongExpression(final ALongExpression expression) {
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
        final VariableInfo localInfo, paramInfo;
        final ClassInfo classInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            types.put(expression, localInfo.getType());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            types.put(expression, paramInfo.getType());
        } else if ((classInfo = symbolTable.getClassInfo(id)) != null) {
            types.put(expression, classInfo.getType());
        } else {
            final int line = expression.getIdentifier().getLine();
            final int column = expression.getIdentifier().getPos();
            error(line, column, "undeclared identifier `%s`", id);
        }
    }

    public void outAThisExpression(final AThisExpression expression) {
        types.put(expression, currentClass.getType());
    }
}
