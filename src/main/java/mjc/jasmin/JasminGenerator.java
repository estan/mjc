package mjc.jasmin;

import java.util.Map;

import mjc.analysis.AnalysisAdapter;
import mjc.node.AArrayAccessExpression;
import mjc.node.AArrayAssignStatement;
import mjc.node.AArrayLengthExpression;
import mjc.node.AAssignStatement;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AFalseExpression;
import mjc.node.AFieldDeclaration;
import mjc.node.AIdentifierExpression;
import mjc.node.AIntegerExpression;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AMethodInvocationExpression;
import mjc.node.AMinusExpression;
import mjc.node.ANewInstanceExpression;
import mjc.node.ANewIntArrayExpression;
import mjc.node.ANotExpression;
import mjc.node.APlusExpression;
import mjc.node.APrintlnStatement;
import mjc.node.AProgram;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
import mjc.node.Node;
import mjc.node.Start;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.types.Type;

/**
 * Jasmin code generator.
 *
 * TODO: More docs.
 */
public class JasminGenerator extends AnalysisAdapter {
    private final static int MAX_STACK_SIZE = 30; // TODO: Don't hardcode this.

    private final JasminHandler handler;
    private StringBuilder result;

    private SymbolTable symbolTable;
    private Map<Node, Type> types;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    public JasminGenerator(JasminHandler handler) {
        this.handler = handler;
    }

    public void generate(Node ast, SymbolTable symbolTable, Map<Node, Type> types) {
        this.symbolTable = symbolTable;
        this.types = types;

        ast.apply(this);
    }

    /**
     * Adds a Jasmin directive to the result.
     *
     * @param directive Directive to add, including any format specifiers.
     * @param args Arguments matching format specifiers in @a directive.
     */
    private void direc(String directive, Object... args) {
        result.append('.' + String.format(directive, args) + '\n');
    }

    /**
     * Adds a Jasmin instruction to the result.
     *
     * @param instruction Instruction to add, including any format specifiers.
     * @param args Arguments matching format specifiers in @a instruction.
     */
    private void instr(String instruction, Object... args) {
        result.append("    " + String.format(instruction, args) + '\n');
    }

    /**
     * Adds a Jasmin label to the result.
     *
     * @param label Label to add.
     */
    private void label(String label) {
        result.append(label + ":\n");
    }

    /**
     * Adds a newline to the result.
     */
    private void nl() {
        result.append("\n");
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

        result = new StringBuilder();

        direc("class public %s", currentClass.getName());
        direc("super java/lang/Object");
        nl();

        // Default constructor.
        direc("method public <init>()V");
        instr("aload_0");
        instr("invokenonvirtual java/lang/Object/<init>()V");
        instr("return");
        direc("end method");
        nl();

        // Main method.
        direc("method public static main([Ljava/lang/String;)V");
        direc("limit locals %d", 1 + currentMethod.getNumVariables());
        direc("limit stack %d", MAX_STACK_SIZE);
        for (Node variableDeclaration : declaration.getLocals()) {
            variableDeclaration.apply(this);
        }
        for (Node statement : declaration.getStatements()) {
            statement.apply(this);
        }
        instr("return");
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

        direc("class public %s", currentClass.getName());
        direc("super java/lang/Object");
        for (Node fieldDeclaration : declaration.getFields()) {
            fieldDeclaration.apply(this);
        }

        // Default constructor.
        nl();
        direc("method public <init>()V");
        instr("aload_0");
        instr("invokenonvirtual java/lang/Object/<init>()V");
        instr("return");
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

        direc("field protected %s %s", fieldName, fieldType.descriptor());
    }

    @Override
    public void caseAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();

        nl();
        direc("method public %s%s", currentMethod.getName(), currentMethod.descriptor());
        direc("limit locals %d", 1 + currentMethod.getNumVariables());
        direc("limit stack %d", MAX_STACK_SIZE);
        for (Node formalDeclaration : declaration.getFormals()) {
            formalDeclaration.apply(this);
        }
        for (Node statement : declaration.getStatements()) {
            statement.apply(this);
        }
        declaration.getReturnExpression().apply(this);
        instr(currentMethod.getReturnType().isReference() ? "areturn" : "ireturn");
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

        instr("getstatic java/lang/System/out Ljava/io/PrintStream;");
        statement.getValue().apply(this);
        instr("invokestatic java/lang/String/valueOf(%s)Ljava/lang/String;", typeDescriptor);
        instr("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    @Override
    public void caseAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getName().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            statement.getValue().apply(this);
            instr("%s %d", type.isReference() ? "astore" : "istore", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            statement.getValue().apply(this);
            instr("%s %d", type.isReference() ? "astore" : "istore", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr("aload_0");
            statement.getValue().apply(this);
            instr("putfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void caseAArrayAssignStatement(final AArrayAssignStatement statement) {
        final String id = statement.getName().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            instr("aload %d", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            instr("aload %d", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr("aload_0");
            instr("getfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
        statement.getIndex().apply(this);
        statement.getValue().apply(this);
        instr("iastore");
    }

    @Override
    public void caseAPlusExpression(final APlusExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr("iadd");
    }

    @Override
    public void caseAMinusExpression(final AMinusExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr("isub");
    }

    @Override
    public void caseATimesExpression(final ATimesExpression expression) {
        expression.getLeft().apply(this);
        expression.getRight().apply(this);
        instr("imul");
    }

    @Override
    public void caseANewInstanceExpression(final ANewInstanceExpression expression) {
        final String className = expression.getClassName().getText();
        instr("new %s", className);
        instr("dup");
        instr("invokespecial %s/<init>()V", className);
    }

    @Override
    public void caseANewIntArrayExpression(final ANewIntArrayExpression expression) {
        expression.getSize().apply(this);
        instr("newarray int");
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression expression) {
        instr("ldc %d", Integer.valueOf(expression.getInteger().getText()));
    }

    @Override
    public void caseATrueExpression(final ATrueExpression expression) {
        instr("iconst_1");
    }

    @Override
    public void caseAFalseExpression(final AFalseExpression expression) {
        instr("iconst_0");
    }

    @Override
    public void caseANotExpression(final ANotExpression expression) {
        instr("iconst_1");
        expression.getExpression().apply(this);
        instr("isub");
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
        instr("invokevirtual %s/%s%s", classInfo.getName(), methodInfo.getName(), methodInfo.descriptor());
    }

    @Override
    public void caseAArrayAccessExpression(final AArrayAccessExpression expression) {
        expression.getArray().apply(this);
        expression.getIndex().apply(this);
        instr("iaload");
    }

    @Override
    public void caseAArrayLengthExpression(final AArrayLengthExpression expression) {
        expression.getArray().apply(this);
        instr("arraylength");
    }

    @Override
    public void caseAIdentifierExpression(final AIdentifierExpression expression) {
        final String id = expression.getIdentifier().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            instr("%s %d", type.isReference() ? "aload" : "iload", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            instr("%s %d", type.isReference() ? "aload" : "iload", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = fieldInfo.getType().descriptor();
            instr("aload_0");
            instr("getfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void caseAThisExpression(final AThisExpression expression) {
        instr("aload_0");
    }
}
