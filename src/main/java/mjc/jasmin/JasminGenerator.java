package mjc.jasmin;

import java.util.HashMap;
import java.util.Map;

import mjc.analysis.AnalysisAdapter;
import mjc.node.AAndExpression;
import mjc.node.AArrayAccessExpression;
import mjc.node.AArrayAssignStatement;
import mjc.node.AArrayLengthExpression;
import mjc.node.AAssignStatement;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AEqualExpression;
import mjc.node.AFalseExpression;
import mjc.node.AFieldDeclaration;
import mjc.node.AGreaterEqualThanExpression;
import mjc.node.AGreaterThanExpression;
import mjc.node.AIdentifierExpression;
import mjc.node.AIfElseStatement;
import mjc.node.AIfStatement;
import mjc.node.AIntegerExpression;
import mjc.node.ALessEqualThanExpression;
import mjc.node.ALessThanExpression;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AMethodInvocationExpression;
import mjc.node.AMinusExpression;
import mjc.node.ANewInstanceExpression;
import mjc.node.ANewIntArrayExpression;
import mjc.node.ANotEqualExpression;
import mjc.node.ANotExpression;
import mjc.node.AOrExpression;
import mjc.node.APlusExpression;
import mjc.node.APrintlnStatement;
import mjc.node.AProgram;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
import mjc.node.AWhileStatement;
import mjc.node.Node;
import mjc.node.PExpression;
import mjc.node.Start;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.types.Type;

/**
 * The JasminGenerator class generates Jasmin code from the AST.
 *
 * Construct an instance of the class with a {@link JasminHandler} for handling
 * the output. Then call the {@link #generate(Node, SymbolTable, Map)} method to
 * start the generation.
 */
public class JasminGenerator extends AnalysisAdapter {
    private final static String INDENT = "    ";

    private final JasminHandler handler;
    private StringBuilder result;
    private Map<String, Integer> labelCounters = new HashMap<>();

    private SymbolTable symbolTable;
    private Map<Node, Type> types;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    private int stackSize;
    private int maxStackSize;

    public JasminGenerator(JasminHandler handler) {
        this.handler = handler;
    }

    /**
     * Generates Jasmin code.
     *
     * The handle method of the {@link JasminHandler} passed in during construction
     * will be called for each generated class.
     *
     * @param ast Input AST.
     * @param symbolTable Input symbol table.
     * @param types Input AST node to MiniJava type map.
     */
    public void generate(Node ast, SymbolTable symbolTable, Map<Node, Type> types) {
        this.symbolTable = symbolTable;
        this.types = types;

        ast.apply(this);
    }

    // Helper methods.

    /** Adds Jasmin {@code directive} formatted with {@code args}. */
    private void direc(String directive, Object... args) {
        result.append('.' + String.format(directive, args) + '\n');
    }

    /**
     * Adds a Jasmin {@code instruction} formatted with {@code args} that changes the stack
     * size by {@code stackChange}.
     */
    private void instr(int stackChange, String instruction, Object... args) {
        result.append(INDENT + String.format(instruction, args) + '\n');
        stackSize += stackChange;
        maxStackSize = Math.max(maxStackSize, stackSize);
    }

    /** Adds a Jasmin {@code label}. */
    private void label(String label) {
        result.append(label + ":\n");
    }

    /** Adds a newline. */
    private void nl() {
        result.append("\n");
    }

    /** Adds a jump to {@code trueLabel} or {@code falseLabel} based on {@code expression}. */
    private void jump(PExpression expression, String trueLabel, String falseLabel) {
        if (expression instanceof AOrExpression) {
            final AOrExpression or = (AOrExpression) expression;
            final String rightLabel = nextLabel("or_right");
            jump(or.getLeft(), trueLabel, rightLabel);
            label(rightLabel);
            jump(or.getRight(), trueLabel, falseLabel);
        } else if (expression instanceof AAndExpression) {
            final AAndExpression and = (AAndExpression) expression;
            final String rightLabel = nextLabel("and_right");
            jump(and.getLeft(), rightLabel, falseLabel);
            label(rightLabel);
            jump(and.getRight(), trueLabel, falseLabel);
        } else if (expression instanceof ALessThanExpression) {
            final ALessThanExpression lt = (ALessThanExpression) expression;
            lt.getLeft().apply(this);
            lt.getRight().apply(this);
            instr(-2, "if_icmplt %s", trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof ALessEqualThanExpression) {
            final ALessEqualThanExpression le = (ALessEqualThanExpression) expression;
            le.getLeft().apply(this);
            le.getRight().apply(this);
            instr(-2, "if_icmple %s", trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof AGreaterThanExpression) {
            final AGreaterThanExpression gt = (AGreaterThanExpression) expression;
            gt.getLeft().apply(this);
            gt.getRight().apply(this);
            instr(-2, "if_icmpgt %s", trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof AGreaterEqualThanExpression) {
            final AGreaterEqualThanExpression ge = (AGreaterEqualThanExpression) expression;
            ge.getLeft().apply(this);
            ge.getRight().apply(this);
            instr(-2, "if_icmpge %s", trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof AEqualExpression) {
            final AEqualExpression eq = (AEqualExpression) expression;
            eq.getLeft().apply(this);
            eq.getRight().apply(this);
            instr(-2, "if_%scmpeq %s", types.get(eq.getLeft()).isReference() ? 'a' : 'i', trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof ANotEqualExpression) {
            final ANotEqualExpression ne = (ANotEqualExpression) expression;
            ne.getLeft().apply(this);
            ne.getRight().apply(this);
            instr(-2, "if_%scmpne %s", types.get(ne.getLeft()).isReference() ? 'a' : 'i', trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof ANotExpression) {
            jump(((ANotExpression) expression).getExpression(), falseLabel, trueLabel);
        } else if (expression instanceof AIdentifierExpression ||
                   expression instanceof AMethodInvocationExpression) {
            expression.apply(this);
            instr(1, "iconst_0");
            instr(-2, "if_icmpne %s", trueLabel);
            instr(0, "goto %s", falseLabel);
        } else if (expression instanceof ATrueExpression) {
            instr(0, "goto %s", trueLabel);
        } else if (expression instanceof AFalseExpression) {
            instr(0, "goto %s", falseLabel);
        } else {
            throw new Error("jump: Unknown expression");
        }
    }

    /**
     * Puts 1 or 0 on the stack based on the boolean value of {@code expression}, using
     * {@code labelPrefix} as prefix for introduced labels.
     */
    private void booleanValue(PExpression expression, String labelPrefix) {
        final String trueLabel = nextLabel(labelPrefix);
        final String falseLabel = nextLabel("not_" + labelPrefix);
        final String skipLabel = nextLabel("skip_" + labelPrefix);

        jump(expression, trueLabel, falseLabel);

        label(trueLabel);
        instr(1, "iconst_1");
        instr(0, "goto %s", skipLabel);
        label(falseLabel);
        instr(1, "iconst_0");
        label(skipLabel);
    }

    /** Returns a new unique label with the given {@code prefix}. */
    private String nextLabel(String prefix) {
        Integer number = labelCounters.get(prefix);
        if (number == null) {
            number = 0;
        }
        labelCounters.put(prefix, number + 1);
        return prefix + '_' + number;
    }

    // Visitor methods below.

    @Override
    public void caseStart(final Start start) {
        start.getPProgram().apply(this);
    }

    @Override
    public void caseAProgram(final AProgram program) {
        program.getMainClassDeclaration().apply(this);
        for (Node classDeclaration : program.getClasses()) {
            classDeclaration.apply(this);
        }
    }

    @Override
    public void caseAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMethodName().getText());
        currentMethod.enterBlock();
        labelCounters.clear();

        result = new StringBuilder();

        direc("class public '%s'", currentClass.getName());
        direc("super java/lang/Object");
        nl();

        // Default constructor.
        maxStackSize = 0;
        direc("method public <init>()V");
        instr(1, "aload_0");
        instr(-1, "invokenonvirtual java/lang/Object/<init>()V");
        instr(-stackSize, "return");
        direc("end method");
        nl();

        // Main method.
        maxStackSize = 0;
        direc("method public static main([Ljava/lang/String;)V");
        direc("limit locals %d", 1 + currentMethod.getNumVariables());
        for (Node variableDeclaration : declaration.getLocals()) {
            variableDeclaration.apply(this);
        }
        for (Node statement : declaration.getStatements()) {
            statement.apply(this);
        }
        instr(-stackSize, "return");
        direc("limit stack %d", maxStackSize);
        direc("end method");

        handler.handle(currentClass.getName(), result);

        currentMethod.leaveBlock();
        currentMethod = null;
        currentClass = null;
    }

    @Override
    public void caseAClassDeclaration(final AClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());

        result = new StringBuilder();

        direc("class public '%s'", currentClass.getName());
        direc("super java/lang/Object");
        for (Node fieldDeclaration : declaration.getFields()) {
            fieldDeclaration.apply(this);
        }

        // Default constructor.
        nl();
        maxStackSize = 0;
        direc("method public <init>()V");
        instr(1, "aload_0");
        instr(-1, "invokenonvirtual java/lang/Object/<init>()V");
        instr(-stackSize, "return");
        direc("end method");
        nl();

        for (Node methodDeclaration : declaration.getMethods()) {
            methodDeclaration.apply(this);
        }

        handler.handle(currentClass.getName(), result);

        currentClass = null;
    }

    @Override
    public void caseAFieldDeclaration(final AFieldDeclaration declaration) {
        final String fieldName = declaration.getName().getText();
        final Type fieldType = currentClass.getField(fieldName).getType();

        direc("field protected '%s' %s", fieldName, fieldType.descriptor());
    }

    @Override
    public void caseAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();
        labelCounters.clear();
        maxStackSize = 0;

        nl();
        direc("method public %s%s", currentMethod.getName(), currentMethod.descriptor());
        direc("limit locals %d", 1 + currentMethod.getNumVariables());
        for (Node formalDeclaration : declaration.getFormals()) {
            formalDeclaration.apply(this);
        }
        for (Node statement : declaration.getStatements()) {
            statement.apply(this);
        }
        declaration.getReturnExpression().apply(this);
        instr(-stackSize, currentMethod.getReturnType().isReference() ? "areturn" : "ireturn");
        direc("limit stack %d", maxStackSize);
        direc("end method");

        currentMethod.leaveBlock();
        currentMethod = null;
    }

    @Override
    public void caseABlockStatement(final ABlockStatement block) {
        currentMethod.enterBlock();
        for (Node statement : block.getStatements()) {
            statement.apply(this);
        }
        currentMethod.leaveBlock();
    }

    @Override
    public void caseAPrintlnStatement(final APrintlnStatement statement) {
        String typeDescriptor = types.get(statement.getValue()).descriptor();

        instr(1, "getstatic java/lang/System/out Ljava/io/PrintStream;");
        statement.getValue().apply(this);
        instr(0, "invokestatic java/lang/String/valueOf(%s)Ljava/lang/String;", typeDescriptor);
        instr(-2, "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    @Override
    public void caseAIfStatement(final AIfStatement statement) {
        final String trueLabel = nextLabel("if_true");
        final String falseLabel = nextLabel("if_false");

        jump(statement.getCondition(), trueLabel, falseLabel);
        label(trueLabel);
        statement.getStatement().apply(this);
        label(falseLabel);
    }

    @Override
    public void caseAIfElseStatement(final AIfElseStatement statement) {
        final String trueLabel = nextLabel("if_else_true");
        final String falseLabel = nextLabel("if_else_false");
        final String endLabel = nextLabel("if_else_end");

        jump(statement.getCondition(), trueLabel, falseLabel);
        label(trueLabel);
        statement.getThen().apply(this);
        instr(0, "goto %s", endLabel);
        label(falseLabel);
        statement.getElse().apply(this);
        label(endLabel);
    }

    @Override
    public void caseAWhileStatement(final AWhileStatement statement) {
        final String loopLabel = nextLabel("while");
        final String trueLabel = nextLabel("while_true");
        final String endLabel = nextLabel("while_end");

        label(loopLabel);
        jump(statement.getCondition(), trueLabel, endLabel);
        label(trueLabel);
        statement.getStatement().apply(this);
        instr(0, "goto %s", loopLabel);
        label(endLabel);
    }

    @Override
    public void caseAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getName().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            statement.getValue().apply(this);
            instr(-1, "%s %d", type.isReference() ? "astore" : "istore", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            statement.getValue().apply(this);
            instr(-1, "%s %d", type.isReference() ? "astore" : "istore", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr(1, "aload_0");
            statement.getValue().apply(this);
            instr(-2, "putfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void caseAArrayAssignStatement(final AArrayAssignStatement statement) {
        final String id = statement.getName().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            instr(1, "aload %d", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            instr(1, "aload %d", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr(1, "aload_0");
            instr(0, "getfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
        statement.getIndex().apply(this);
        statement.getValue().apply(this);
        instr(-3, "iastore");
    }

    @Override
    public void caseAAndExpression(final AAndExpression expression) {
        booleanValue(expression, "and");
    }

    @Override
    public void caseAOrExpression(final AOrExpression expression) {
        booleanValue(expression, "or");
    }

    @Override
    public void caseALessThanExpression(final ALessThanExpression expression) {
        booleanValue(expression, "lt");
    }

    @Override
    public void caseALessEqualThanExpression(final ALessEqualThanExpression expression) {
        booleanValue(expression, "le");
    }

    @Override
    public void caseAGreaterThanExpression(final AGreaterThanExpression expression) {
        booleanValue(expression, "gt");
    }

    @Override
    public void caseAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        booleanValue(expression, "ge");
    }

    @Override
    public void caseAEqualExpression(final AEqualExpression expression) {
        booleanValue(expression, "eq");
    }

    @Override
    public void caseANotEqualExpression(final ANotEqualExpression expression) {
        booleanValue(expression, "ne");
    }

    @Override
    public void caseAPlusExpression(final APlusExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr(-1, "iadd");
    }

    @Override
    public void caseAMinusExpression(final AMinusExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr(-1, "isub");
    }

    @Override
    public void caseATimesExpression(final ATimesExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr(-1, "imul");
    }

    @Override
    public void caseANewInstanceExpression(final ANewInstanceExpression expression) {
        final String className = expression.getClassName().getText();
        instr(1, "new '%s'", className);
        instr(1, "dup");
        instr(-1, "invokespecial %s/<init>()V", className);
    }

    @Override
    public void caseANewIntArrayExpression(final ANewIntArrayExpression expression) {
        expression.getSize().apply(this);
        instr(0, "newarray int");
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression expression) {
        instr(1, "ldc %d", Integer.valueOf(expression.getInteger().getText()));
    }

    @Override
    public void caseATrueExpression(final ATrueExpression expression) {
        instr(1, "iconst_1");
    }

    @Override
    public void caseAFalseExpression(final AFalseExpression expression) {
        instr(1, "iconst_0");
    }

    @Override
    public void caseANotExpression(final ANotExpression expression) {
        instr(1, "iconst_1");
        expression.getExpression().apply(this);
        instr(-1, "isub");
    }

    @Override
    public void caseAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        final Type type = types.get(expression.getInstance());
        final ClassInfo classInfo = symbolTable.getClassInfo(type.getName());
        final MethodInfo methodInfo = classInfo.getMethod(expression.getName().getText());

        expression.getInstance().apply(this);
        for (Node actualParameter : expression.getActuals()) {
            actualParameter.apply(this);
        }
        instr(-expression.getActuals().size(), "invokevirtual %s/%s%s",
                classInfo.getName(), methodInfo.getName(), methodInfo.descriptor());
    }

    @Override
    public void caseAArrayAccessExpression(final AArrayAccessExpression expression) {
        expression.getArray().apply(this);
        expression.getIndex().apply(this);
        instr(-1, "iaload");
    }

    @Override
    public void caseAArrayLengthExpression(final AArrayLengthExpression expression) {
        expression.getArray().apply(this);
        instr(0, "arraylength");
    }

    @Override
    public void caseAIdentifierExpression(final AIdentifierExpression expression) {
        final String id = expression.getIdentifier().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            instr(1, "%s %d", type.isReference() ? "aload" : "iload", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            instr(1, "%s %d", type.isReference() ? "aload" : "iload", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr(1, "aload_0");
            instr(0, "getfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void caseAThisExpression(final AThisExpression expression) {
        instr(1, "aload_0");
    }
}
