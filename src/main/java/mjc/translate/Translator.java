package mjc.translate;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Booleans;

import mjc.analysis.AnalysisAdapter;
import mjc.frame.Factory;
import mjc.frame.Frame;
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
import mjc.node.AProgram;
import mjc.node.AThisExpression;
import mjc.node.ATimesExpression;
import mjc.node.ATrueExpression;
import mjc.node.AWhileStatement;
import mjc.node.Node;
import mjc.node.PClassDeclaration;
import mjc.node.PFieldDeclaration;
import mjc.node.PFormalParameter;
import mjc.node.PMethodDeclaration;
import mjc.node.PStatement;
import mjc.node.PVariableDeclaration;
import mjc.node.Start;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.temp.Label;
import mjc.tree.LABEL;
import mjc.tree.SEQ;
import mjc.tree.View;

public class Translator extends AnalysisAdapter {
    private SymbolTable symbolTable;
    private Factory factory;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;
    private Frame currentFrame;
    private TreeNode currentTree;

    private List<ProcFrag> fragments;

    /**
     * Translates the given AST into IR and returns the result as a tree.Exp.
     *
     * @param ast Input AST.
     * @param symbolTable Symbol table for the input program.
     * @param factory Factory for constructing frames and records.
     * @return IR representation of @a ast.
     */
    public List<ProcFrag> translate(final Node ast, final SymbolTable symbolTable, final Factory factory) {
        this.symbolTable = symbolTable;
        this.factory = factory;

        fragments = new ArrayList<>();

        ast.apply(this);

        return fragments;
    }

    /**
     * Translates the given AST node into IR and returns the result.
     *
     * Note: The {@link #currentTree} field will be set to null.
     *
     * @param astNode input AST node.
     * @return IR representation of @a astNode.
     */
    private TreeNode treeOf(final Node astNode) {
        astNode.apply(this);
        final TreeNode result = this.currentTree;
        this.currentTree = null;
        return result;
    }

    @Override
    public void caseStart(final Start start) {
        start.getPProgram().apply(this);
    }

    @Override
    public void caseAProgram(final AProgram program) {
        System.out.println("Translating program");
        program.getMainClassDeclaration().apply(this);
        for (PClassDeclaration classDeclaration : program.getClasses()) {
            classDeclaration.apply(this);
        }
    }

    @Override
    public void caseAMainClassDeclaration(final AMainClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());
        currentMethod = currentClass.getMethod(declaration.getMethodName().getText());
        currentMethod.enterBlock();
        currentFrame = factory.newFrame(
                new Label(currentClass.getName() + '$' + currentMethod.getName()),
                new ArrayList<Boolean>()
        );

        System.out.println("Translating " + currentClass.getName());

        // TODO: Construct a ProcFrag from the locals, statements, and the current
        //       frame, then add it to the fragments list.

        for (PVariableDeclaration variableDeclaration : declaration.getLocals()) {
            variableDeclaration.apply(this);
        }

        for (PStatement statement : declaration.getStatements()) {
            statement.apply(this);
        }

        currentFrame = null;
        currentMethod.leaveBlock();
        currentMethod = null;
        currentClass = null;
    }

    @Override
    public void caseAClassDeclaration(final AClassDeclaration declaration) {
        currentClass = symbolTable.getClassInfo(declaration.getName().getText());

        for (PFieldDeclaration fieldDeclaration : declaration.getFields()) {
            fieldDeclaration.apply(this);
        }

        for (PMethodDeclaration methodDeclaration : declaration.getMethods()) {
            methodDeclaration.apply(this);
        }

        currentClass = null;
    }

    @Override
    public void caseAMethodDeclaration(final AMethodDeclaration declaration) {
        currentMethod = currentClass.getMethod(declaration.getName().getText());
        currentMethod.enterBlock();
        currentFrame = factory.newFrame(
                new Label(currentClass.getName() + '$' + currentMethod.getName()),
                Booleans.asList(new boolean[currentMethod.getParameters().size()])
        );

        System.out.println("Translating " + currentMethod.getName());

        // TODO: Construct a ProcFrag from the formals, locals, statements,
        //       return expression and the current frame, then add it to the
        //       fragments list.

        for (PFormalParameter formalParameter : declaration.getFormals()) {
            formalParameter.apply(this);
        }

        for (PVariableDeclaration variableDeclaration : declaration.getLocals()) {
            variableDeclaration.apply(this);
        }

        for (PStatement statement : declaration.getStatements()) {
            System.out.println("bja");
            statement.apply(this);
        }

        declaration.getReturnExpression().apply(this);

        currentFrame = null;
        currentMethod.leaveBlock();
        currentMethod = null;
    }

    @Override
    public void caseABlockStatement(final ABlockStatement block) {
        currentMethod.enterBlock();

        for (PVariableDeclaration variableDeclaration : block.getLocals()) {
            variableDeclaration.apply(this);
        }

        for (PStatement statement : block.getStatements()) {
            statement.apply(this);
        }

        currentMethod.leaveBlock();
    }

    @Override
    public void caseAIfStatement(final AIfStatement statement) {
        currentTree = new If(
            treeOf(statement.getCondition()),
            treeOf(statement.getStatement())
        );
    }

    @Override
    public void caseAIfElseStatement(final AIfElseStatement statement) {
        System.out.println("Translating if-else");
        currentTree = new IfElse(
            treeOf(statement.getCondition()),
            treeOf(statement.getThen()),
            treeOf(statement.getElse())
        );

        /*
        View viewer = new View("if-else");
        viewer.addStm(currentTree.asStm());
        viewer.expandTree();
        */
    }

    @Override
    public void caseAWhileStatement(final AWhileStatement statement) {
        final Label test = new Label();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        currentTree = new StmNode(
            new SEQ(new LABEL(test),
            new SEQ(treeOf(statement.getCondition()).asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(trueLabel),
            new SEQ(treeOf(statement.getStatement()).asStm(),
                    new LABEL(falseLabel))))));
    }

    @Override
    public void caseAPrintlnStatement(final APrintlnStatement statement) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAAssignStatement(final AAssignStatement statement) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAArrayAssignStatement(final AArrayAssignStatement statement) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAAndExpression(final AAndExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAOrExpression(final AOrExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseALessThanExpression(final ALessThanExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAGreaterThanExpression(final AGreaterThanExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseALessEqualThanExpression(final ALessEqualThanExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAEqualExpression(final AEqualExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseANotEqualExpression(final ANotEqualExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAPlusExpression(final APlusExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAMinusExpression(final AMinusExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseATimesExpression(final ATimesExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseANotExpression(final ANotExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAArrayAccessExpression(final AArrayAccessExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAArrayLengthExpression(final AArrayLengthExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseANewInstanceExpression(final ANewInstanceExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseANewIntArrayExpression(final ANewIntArrayExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseANewLongArrayExpression(final ANewLongArrayExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseALongExpression(final ALongExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseATrueExpression(final ATrueExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAFalseExpression(final AFalseExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAIdentifierExpression(final AIdentifierExpression expression) {
        // TODO
        currentTree = new TODONode();
    }

    @Override
    public void caseAThisExpression(final AThisExpression expression) {
        // TODO
        currentTree = new TODONode();
    }
}
