package mjc.jasmin;

import java.util.Map;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.AAssignStatement;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AFalseExpression;
import mjc.node.AFieldDeclaration;
import mjc.node.AIdentifierExpression;
import mjc.node.AIntegerExpression;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.AMinusExpression;
import mjc.node.ANotExpression;
import mjc.node.APlusExpression;
import mjc.node.APrintlnStatement;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
import mjc.node.Node;
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
public class JasminGenerator extends DepthFirstAdapter {
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

    /**
     * Returns a Jasmin type descriptor for the given MiniJava type.
     *
     * @param type A MiniJava type.
     * @return The Jasmin type descriptor.
     */
    private String typeDescriptor(final Type type) {
        if (type.isInt()) {
            return "I";
        } else if (type.isIntArray()) {
            return "[I";
        } else if (type.isBoolean()) {
            return "Z";
        } else if (type.isClass()) {
            return "L" + type.getName() + ";";
        } else {
            throw new Error("Unknown Type");
        }
    }

    /**
     * Returns a Jasmin method signature descriptor for the given method.
     *
     * @param methodInfo Method information.
     * @return A Jasmin method signature descriptor.
     */
    private String methodSignatureDescriptor(final MethodInfo methodInfo) {
        String descriptor = "(";
        for (VariableInfo paramInfo : methodInfo.getParameters()) {
            descriptor += typeDescriptor(paramInfo.getType());
        }
        descriptor += ")" + typeDescriptor(methodInfo.getReturnType());
        return descriptor;
    }

    // Visitor methods below.

    @Override
    public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
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
        direc("limit locals %d", currentMethod.getNextIndex());
        direc("limit stack %d", MAX_STACK_SIZE);
    }

    @Override
    public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
        // End of main method.
        instr("return");
        direc("end method");

        // Handle the result using the configured handler.
        handler.handle(currentClass.getName(), result);

        currentMethod.leaveBlock();
        currentMethod = null;
        currentClass = null;
    }

    @Override
    public void inAClassDeclaration(final AClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());

        result = new StringBuilder();

        direc("class public %s", currentClass.getName());
        direc("super java/lang/Object");
        nl();
    }

    @Override
    public void outAClassDeclaration(final AClassDeclaration declaration) {
        // Default constructor.
        direc("method public <init>()V");
        instr("aload_0");
        instr("invokenonvirtual java/lang/Object/<init>()V");
        instr("return");
        direc("end method");
        nl();

        // Handle the result using the configured handler.
        handler.handle(currentClass.getName(), result);

        currentClass = null;
    }

    @Override
    public void inAFieldDeclaration(final AFieldDeclaration declaration) {
        final String fieldName = declaration.getName().getText();
        final Type fieldType = currentClass.getField(fieldName).getType();

        direc("field protected %s %s", fieldName, typeDescriptor(fieldType));
    }

    @Override
    public void inAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();

        final String methodName = currentMethod.getName();
        final String methodSignature = methodSignatureDescriptor(currentMethod);

        nl();
        direc("method public %s%s", methodName, methodSignature);
        direc("limit locals %d", currentMethod.getNextIndex() + 1);
        direc("limit stack %d", MAX_STACK_SIZE);
    }

    @Override
    public void outAMethodDeclaration(final AMethodDeclaration declaration) {
        final Type type = currentMethod.getReturnType();

        instr(type.isReference() ? "areturn" : "ireturn");
        direc("end method");

        currentMethod.leaveBlock();
        currentMethod = null;
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
    public void inAPrintlnStatement(final APrintlnStatement statement) {
        instr("getstatic java/lang/System/out Ljava/io/PrintStream;");
    }

    @Override
    public void outAPrintlnStatement(final APrintlnStatement statement) {
        String typeDescriptor = typeDescriptor(types.get(statement.getValue()));

        instr("invokestatic java/lang/String/valueOf(%s)Ljava/lang/String;", typeDescriptor);
        instr("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    @Override
    public void inAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getName().getText();

        if (currentMethod.getLocal(id) == null && currentMethod.getParameter(id) == null) {
            // Load this pointer in preparation for putfield.
            instr("aload_0");
        }
    }

    @Override
    public void outAAssignStatement(final AAssignStatement statement) {
        final String id = statement.getName().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            instr("%s %d", type.isReference() ? "astore" : "istore", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            instr("%s %d", type.isReference() ? "astore" : "istore", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = typeDescriptor(fieldInfo.getType());
            instr("putfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void outAPlusExpression(final APlusExpression expression) {
        instr("iadd");
    }

    @Override
    public void outAMinusExpression(final AMinusExpression expression) {
        instr("isub");
    }

    @Override
    public void outATimesExpression(final ATimesExpression expression) {
        instr("imul");
    }

    @Override
    public void outAIntegerExpression(final AIntegerExpression expression) {
        instr("ldc %d", Integer.valueOf(expression.getInteger().getText()));
    }

    @Override
    public void outATrueExpression(final ATrueExpression expression) {
        instr("iconst_1");
    }

    @Override
    public void inANotExpression(final ANotExpression expression) {
        instr("iconst_1");
    }

    @Override
    public void outANotExpression(final ANotExpression expression) {
        instr("isub");
    }

    @Override
    public void outAFalseExpression(final AFalseExpression expression) {
        instr("iconst_0");
    }

    @Override
    public void outAIdentifierExpression(final AIdentifierExpression expression) {
        final String id = expression.getIdentifier().getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(id)) != null) {
            final Type type = localInfo.getType();
            instr("%s %d", type.isReference() ? "aload" : "iload", localInfo.getIndex());
        } else if ((paramInfo = currentMethod.getParameter(id)) != null) {
            final Type type = paramInfo.getType();
            instr("%s %d", type.isReference() ? "aload" : "iload", paramInfo.getIndex());
        } else if ((fieldInfo = currentClass.getField(id)) != null) {
            final String typeDescriptor = typeDescriptor(fieldInfo.getType());
            instr("aload_0");
            instr("getfield %s/%s %s", currentClass.getName(), id, typeDescriptor);
        }
    }

    @Override
    public void outAThisExpression(final AThisExpression expression) {
        instr("aload_0");
    }
}
