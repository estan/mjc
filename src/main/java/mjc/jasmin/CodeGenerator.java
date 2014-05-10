package mjc.jasmin;

import java.io.IOException;
import java.util.Map;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AFalseExpression;
import mjc.node.AFieldDeclaration;
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
import mjc.node.AVariableDeclaration;
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
public class CodeGenerator extends DepthFirstAdapter {
    private final static int MAX_STACK_SIZE = 30; // TODO: Don't hardcode this.

    private SymbolTable symbolTable;
    private Map<Node, Type> types;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    private JasminWriter out = new JasminWriter();

    public CodeGenerator(SymbolTable symbolTable, Map<Node, Type> types) {
        this.symbolTable = symbolTable;
        this.types = types;
    }

    private String typeDescriptor(final Type type) {
        if (type.isInt()) {
            return "I";
        } else if (type.isIntArray()) {
            return "[I";
        } else if (type.isBoolean()) {
            return "B";
        } else if (type.isClass()) {
            return "L" + type.getName() + ";";
        } else {
            throw new Error("Unknown Type");
        }
    }

    private void writeClass() {
        // Just write to standard output for now.
        try {
            out.write(System.out);
            System.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.clear();
    }

    private String methodSignatureDescriptor(final MethodInfo methodInfo) {
        String descriptor = "(";
        for (VariableInfo paramInfo : methodInfo.getParameters()) {
            descriptor += typeDescriptor(paramInfo.getType());
        }
        descriptor += ")" + typeDescriptor(methodInfo.getReturnType());
        return descriptor;
    }

    @Override
    public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMethodName().getText());
        currentMethod.enterBlock();

        out.directive("class public %s", currentClass.getName());
        out.directive("super java/lang/Object");
        out.newline();

        out.bigcomment("Default constructor");
        out.directive("method public <init>()V");
        out.aload(0);
        out.instruction("invokenonvirtual java/lang/Object/<init>()V");
        out.instruction("return");
        out.directive("end method");
        out.newline();

        final int numLocals = currentMethod.getNumParameters() + currentMethod.getNumLocals() + 1;

        out.bigcomment("Main method");
        out.directive("method public static main([Ljava/lang/String;)V");
        out.directive("limit locals %d", numLocals);
        out.directive("limit stack %d", MAX_STACK_SIZE);
    }

    @Override
    public void outAMainClassDeclaration(final AMainClassDeclaration declaration) {
        out.instruction("return");
        out.directive("end method");

        writeClass();

        currentMethod.leaveBlock();
        currentMethod = null;
        currentClass = null;
    }

    @Override
    public void inAClassDeclaration(final AClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());

        out.directive("class public %s", currentClass.getName());
        out.directive("super java/lang/Object");
    }

    @Override
    public void outAClassDeclaration(final AClassDeclaration declaration) {

        out.newline();
        out.bigcomment("Default constructor");
        out.directive("method public <init>()V");
        out.aload(0);
        out.instruction("invokenonvirtual java/lang/Object/<init>()V");
        out.instruction("return");
        out.directive("end method");
        out.newline();

        writeClass();

        currentClass = null;
    }

    @Override
    public void inAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();

        final String methodName = currentMethod.getName();
        final String methodSignature = methodSignatureDescriptor(currentMethod);
        final int numLocals = currentMethod.getNumParameters() + currentMethod.getNumLocals() + 1;

        out.newline();
        out.bigcomment("Method " + methodName);
        out.directive("method public %s%s", methodName, methodSignature);
        out.directive("limit locals %d", numLocals);
        out.directive("limit stack %d", MAX_STACK_SIZE);
    }

    @Override
    public void outAMethodDeclaration(final AMethodDeclaration declaration) {
        final Type type = currentMethod.getReturnType();

        out.instruction(type.isClass() || type.isIntArray() ? "areturn" : "ireturn");
        out.directive("end method");

        currentMethod.leaveBlock();
        currentMethod = null;
    }

    @Override
    public void inAFieldDeclaration(final AFieldDeclaration declaration) {
        final String fieldName = declaration.getName().getText();
        final Type fieldType = currentClass.getField(fieldName).getType();

        out.directive("field protected %s %s", typeDescriptor(fieldType), fieldName);
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
        out.instruction("getstatic java/lang/System/out Ljava/io/PrintStream;");
    }

    @Override
    public void outAPrintlnStatement(final APrintlnStatement statement) {
        out.instruction("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
        out.instruction("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    @Override
    public void outAPlusExpression(final APlusExpression expression) {
        out.instruction("iadd");
    }

    @Override
    public void outAMinusExpression(final AMinusExpression expression) {
        out.instruction("isub");
    }

    @Override
    public void outATimesExpression(final ATimesExpression expression) {
        out.instruction("imul");
    }

    @Override
    public void outAIntegerExpression(final AIntegerExpression expression) {
        out.iconst(Integer.valueOf(expression.getInteger().getText()));
    }

    @Override
    public void outATrueExpression(final ATrueExpression expression) {
        out.iconst(1);
    }

    @Override
    public void inANotExpression(final ANotExpression expression) {
        out.iconst(1);
    }

    @Override
    public void outANotExpression(final ANotExpression expression) {
        out.instruction("isub");
    }

    @Override
    public void outAFalseExpression(final AFalseExpression expression) {
        out.iconst(0);
    }

    @Override
    public void outAThisExpression(final AThisExpression expression) {
        out.aload(0);
    }
}
