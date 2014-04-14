package mjc.translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.primitives.Booleans;

import mjc.analysis.AnalysisAdapter;
import mjc.frame.Access;
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
import mjc.node.AFieldDeclaration;
import mjc.node.AFormalParameter;
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
import mjc.node.AVariableDeclaration;
import mjc.node.AWhileStatement;
import mjc.node.Node;
import mjc.node.PClassDeclaration;
import mjc.node.PFieldDeclaration;
import mjc.node.PMethodDeclaration;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.temp.Label;
import mjc.tree.BINOP;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.DCONST;
import mjc.tree.Exp;
import mjc.tree.ExpList;
import mjc.tree.LABEL;
import mjc.tree.MOVE;
import mjc.tree.SEQ;
import mjc.tree.TEMP;

public class Translator extends AnalysisAdapter {
    private SymbolTable symbolTable;
    private Factory factory;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;
    private Frame currentFrame;
    private Translation currentTree;

    private List<ProcFrag> fragments;

    /**
     * Translates the given AST into IR and returns the result as a list of procedure fragments.
     *
     * @param ast Input AST.
     * @param symbolTable Symbol table for the input program.
     * @param factory Factory for constructing frames and records.
     * @return List of procedure fragments.
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
    private Translation treeOf(final Node astNode) {
        astNode.apply(this);
        final Translation result = this.currentTree;
        this.currentTree = null;
        return result;
    }

    @Override
    public void caseStart(final Start start) {
        start.getPProgram().apply(this);
    }

    @Override
    public void caseAProgram(final AProgram program) {
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

        final LinkedList<Node> nodes = new LinkedList<>();
        nodes.addAll(declaration.getLocals());
        nodes.addAll(declaration.getStatements());

        Translation tree = buildStm(nodes);

        if (tree != null) {
            currentTree = tree;
            fragments.add(new ProcFrag(currentFrame.procEntryExit1(currentTree.asStm()), currentFrame));
        } else {
            currentTree = new Expression(new CONST(0));
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

        final LinkedList<Node> nodes = new LinkedList<>();
        nodes.addAll(declaration.getFormals());
        nodes.addAll(declaration.getLocals());
        nodes.addAll(declaration.getStatements());
        nodes.add(declaration.getReturnExpression());

        Translation tree = buildStm(nodes);

        if (tree != null) {
            currentTree = tree;
            fragments.add(new ProcFrag(currentFrame.procEntryExit1(currentTree.asStm()), currentFrame));
        }

        currentFrame = null;
        currentMethod.leaveBlock();
        currentMethod = null;
    }

    @Override
    public void caseAFieldDeclaration(final AFieldDeclaration declaration) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseAFormalParameter(final AFormalParameter declaration) {
        final VariableInfo paramInfo = currentMethod.getParameter(declaration.getName().getText());
        final Access access = currentFrame.allocLocal(false);

        paramInfo.setAccess(access);

        currentTree = new Expression(access.exp(new TEMP(currentFrame.FP())));
    }

    @Override
    public void caseAVariableDeclaration(final AVariableDeclaration declaration) {
        final VariableInfo variableInfo = currentMethod.getLocal(declaration.getName().getText());
        final Access access = currentFrame.allocLocal(false);

        variableInfo.setAccess(access);

        currentTree = new Expression(access.exp(new TEMP(currentFrame.FP())));
    }

    @Override
    public void caseABlockStatement(final ABlockStatement block) {
        currentMethod.enterBlock();

        final LinkedList<Node> nodes = new LinkedList<>();
        nodes.addAll(block.getLocals());
        nodes.addAll(block.getStatements());
        Translation tree = buildStm(nodes);

        if (tree != null) {
            currentTree = tree;
        } else {
            currentTree = new Expression(new CONST(0));
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
        currentTree = new IfElse(
            treeOf(statement.getCondition()),
            treeOf(statement.getThen()),
            treeOf(statement.getElse())
        );
    }

    @Override
    public void caseAWhileStatement(final AWhileStatement statement) {
        final Label test = new Label();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        currentTree = new Statement(
            new SEQ(new LABEL(test),
            new SEQ(treeOf(statement.getCondition()).asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(trueLabel),
            new SEQ(treeOf(statement.getStatement()).asStm(),
                    new LABEL(falseLabel))))));
    }

    @Override
    public void caseAPrintlnStatement(final APrintlnStatement statement) {
        currentTree = new Expression(
            currentFrame.externalCall("_minijavalib_println",
            new ExpList(treeOf(statement.getValue()).asExp())));
    }

    @Override
    public void caseAAssignStatement(final AAssignStatement statement) {
        currentTree = new Statement(
            new MOVE(getVariable(statement.getName()), treeOf(statement.getValue()).asExp()));
    }

    @Override
    public void caseAArrayAssignStatement(final AArrayAssignStatement statement) {
        currentTree = new Statement(
            new MOVE(new BINOP(
                    BINOP.PLUS,
                    getVariable(statement.getName()),
                    treeOf(statement.getIndex()).asExp()),
                treeOf(statement.getValue()).asExp()));
    }

    @Override
    public void caseAAndExpression(final AAndExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseAOrExpression(final AOrExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseALessThanExpression(final ALessThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.LT,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseAGreaterThanExpression(final AGreaterThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.GT,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.GE,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseALessEqualThanExpression(final ALessEqualThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.LE,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseAEqualExpression(final AEqualExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.EQ,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseANotEqualExpression(final ANotEqualExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.NE,
            treeOf(expression.getLeft()),
            treeOf(expression.getRight()));
    }

    @Override
    public void caseAPlusExpression(final APlusExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.PLUS,
            treeOf(expression.getLeft()).asExp(),
            treeOf(expression.getRight()).asExp()));
    }

    @Override
    public void caseAMinusExpression(final AMinusExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.MINUS,
            treeOf(expression.getLeft()).asExp(),
            treeOf(expression.getRight()).asExp()));
    }

    @Override
    public void caseATimesExpression(final ATimesExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.MUL,
            treeOf(expression.getLeft()).asExp(),
            treeOf(expression.getRight()).asExp()));
    }

    @Override
    public void caseANotExpression(final ANotExpression expression) {
        currentTree = new Expression(new BINOP(
            BINOP.MINUS,
            new CONST(1),
            treeOf(expression.getExpression()).asExp()));
    }

    @Override
    public void caseAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseAArrayAccessExpression(final AArrayAccessExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseAArrayLengthExpression(final AArrayLengthExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseANewInstanceExpression(final ANewInstanceExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseANewIntArrayExpression(final ANewIntArrayExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseANewLongArrayExpression(final ANewLongArrayExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression expression) {
        currentTree = new Expression(
            new CONST(Integer.parseInt(expression.getInteger().getText())));
    }

    @Override
    public void caseALongExpression(final ALongExpression expression) {
        final String literal = expression.getLong().getText();
        currentTree = new Expression(
            new DCONST(Long.parseLong(literal.substring(0, literal.length() - 1)))); // Strip 'L'/'l'.
    }

    @Override
    public void caseATrueExpression(final ATrueExpression expression) {
        currentTree = new Expression(new CONST(1));
    }

    @Override
    public void caseAFalseExpression(final AFalseExpression expression) {
        currentTree = new Expression(new CONST(0));
    }

    @Override
    public void caseAIdentifierExpression(final AIdentifierExpression expression) {
        currentTree = new Expression(getVariable(expression.getIdentifier()));
    }

    @Override
    public void caseAThisExpression(final AThisExpression expression) {
        // TODO
        currentTree = new TODO();
    }

    private Exp getVariable(TIdentifier id) {
        final String name = id.getText();

        final VariableInfo localInfo, paramInfo, fieldInfo;
        if ((localInfo = currentMethod.getLocal(name)) != null) {
            return localInfo.getAccess().exp(new TEMP(currentFrame.FP()));
        } else if ((paramInfo = currentMethod.getParameter(name)) != null) {
            return paramInfo.getAccess().exp(new TEMP(currentFrame.FP()));
        } else if ((fieldInfo = currentClass.getField(name)) != null) {
            // TODO
            return new TODO().asExp();
        } else {
            throw new Error("No such symbol: " + name);
        }
    }

    private Translation buildStm(LinkedList<Node> statements) {
        if (statements.isEmpty())
            return null;

        if (statements.size() == 1)
            return treeOf(statements.get(0));

        final Iterator<Node> it = statements.iterator();

        SEQ result = new SEQ(treeOf(it.next()).asStm(), null);
        SEQ current = result;
        while (it.hasNext()) {
            Node next = it.next();
            if (it.hasNext()) {
                current.right = new SEQ(treeOf(next).asStm(), null);
                current = (SEQ) current.right;
            } else {
                current.right = treeOf(next).asStm();
            }
        }

        return new Statement(result);
    }
}
