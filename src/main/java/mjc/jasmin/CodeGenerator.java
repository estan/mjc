package mjc.jasmin;

import java.io.IOException;
import java.util.Map;

import mjc.analysis.DepthFirstAdapter;
import mjc.node.ABlockStatement;
import mjc.node.AClassDeclaration;
import mjc.node.AMainClassDeclaration;
import mjc.node.AMethodDeclaration;
import mjc.node.Node;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.types.Type;

/**
 * Jasmin code generator.
 *
 * TODO: More docs.
 */
public class CodeGenerator extends DepthFirstAdapter {
    private SymbolTable symbolTable;
    private Map<Node, Type> types;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;

    private JasminWriter out = new JasminWriter();

    public CodeGenerator(SymbolTable symbolTable, Map<Node, Type> types) {
        this.symbolTable = symbolTable;
        this.types = types;
    }

    @Override
    public void inAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMethodName().getText());
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

        out.descriptor("class public %s", currentClass.getName());
        out.descriptor("super java/lang/Object");
    }

    @Override
    public void outAClassDeclaration(final AClassDeclaration declaration) {
        // Just write to standard output for now.
        try {
            out.write(System.out);
            System.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.clear();

        currentClass = null;
    }

    @Override
    public void inAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();
    }

    @Override
    public void outAMethodDeclaration(final AMethodDeclaration declaration) {
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
}
