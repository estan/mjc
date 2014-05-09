package mjc.translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.Booleans;

import mjc.types.Type;
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
import mjc.node.AVariableDeclaration;
import mjc.node.AWhileStatement;
import mjc.node.Node;
import mjc.node.PClassDeclaration;
import mjc.node.PExpression;
import mjc.node.PFieldDeclaration;
import mjc.node.PMethodDeclaration;
import mjc.node.Start;
import mjc.node.TIdentifier;
import mjc.symbol.ClassInfo;
import mjc.symbol.MethodInfo;
import mjc.symbol.SymbolTable;
import mjc.symbol.VariableInfo;
import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.tree.BINOP;
import mjc.tree.CALL;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.ESEQ;
import mjc.tree.Exp;
import mjc.tree.ExpList;
import mjc.tree.LABEL;
import mjc.tree.MEM;
import mjc.tree.MOVE;
import mjc.tree.NAME;
import mjc.tree.SEQ;
import mjc.tree.TEMP;

public class Translator extends AnalysisAdapter {
    private SymbolTable symbolTable;
    private Map<Node, Type> types;
    private Factory factory;

    private ClassInfo currentClass;
    private MethodInfo currentMethod;
    private Frame currentFrame;
    private Translation currentTree;

    private List<ProcFrag> fragments;

    /**
     * Translates the given AST into IR and returns the result as a list of procedure
     * fragments. This is the main API for this class.
     *
     * @param ast Input AST.
     * @param symbolTable Symbol table for the input program.
     * @param factory Factory for constructing frames and records.
     * @return List of procedure fragments.
     */
    public List<ProcFrag> translate(final Node ast, final SymbolTable symbolTable, final Map<Node, Type> types, final Factory factory) {
        this.symbolTable = symbolTable;
        this.types = types;
        this.factory = factory;

        this.currentClass = null;
        this.currentMethod = null;
        this.currentFrame = null;
        this.currentTree = null;

        fragments = new ArrayList<>();

        ast.apply(this);

        return fragments;
    }

    /**
     * Translates the given AST node into IR and returns the result.
     *
     * Note: The {@link #currentTree} field will be set to null.
     *
     * @param node An AST node.
     * @return The translation of @a node.
     */
    private Translation translate(final Node node) {
        node.apply(this);
        final Translation result = this.currentTree;
        this.currentTree = null;
        return result;
    }

    /**
     * Translates the given AST identifier into IR and returns the result.
     *
     * The identifier must be a field, parameter or local variable.
     *
     * @param id A field, parameter or local variable identifier.
     * @return The translation of @a id.
     */
    private Exp translate(final TIdentifier id) {
        final String name = id.getText();
        final VariableInfo localInfo, paramInfo, fieldInfo;

        if ((localInfo = currentMethod.getLocal(name)) != null) {
            return localInfo.getAccess().exp(new TEMP(currentFrame.FP()));
        } else if ((paramInfo = currentMethod.getParameter(name)) != null) {
            return paramInfo.getAccess().exp(new TEMP(currentFrame.FP()));
        } else if ((fieldInfo = currentClass.getField(name)) != null) {
            return new BINOP(
                BINOP.PLUS,
                currentFrame.formals().get(0).exp(new TEMP(currentFrame.FP())),
                new BINOP(
                    BINOP.MUL,
                    new CONST(fieldInfo.getIndex()),
                    new CONST(currentFrame.wordSize()
                ))
            );
        } else {
            throw new Error("No such symbol: " + name);
        }
    }

    /**
     * Translates the given list of AST nodes into IR and returns the result.
     *
     * If @a nodes is empty, null is returned. If @a nodes has a single element, the
     * translation of this element is returned. Otherwise SEQ(t0, SEQ(t1, SEQ(...)))
     * is returned, where t0, t1, ... is the translation of node[0], node[1], ...
     *
     * @param nodes A list of AST nodes.
     * @return The translation of @a nodes as described above.
     */
    private Translation translate(final List<Node> nodes) {
        if (nodes.isEmpty())
            return null;

        if (nodes.size() == 1)
            return translate(nodes.get(0));

        final Iterator<Node> it = nodes.iterator();

        SEQ result = new SEQ(translate(it.next()).asStm(), null);
        SEQ current = result;
        while (it.hasNext()) {
            Node next = it.next();
            if (it.hasNext()) {
                current.right = new SEQ(translate(next).asStm(), null);
                current = (SEQ) current.right;
            } else {
                current.right = translate(next).asStm();
            }
        }

        return new Statement(result);
    }

    // AST visitor methods below.

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
                Booleans.asList(new boolean[1])
        );

        final List<Node> nodes = new LinkedList<>();
        nodes.addAll(declaration.getLocals());
        nodes.addAll(declaration.getStatements());

        if (!nodes.isEmpty()) {
            currentTree = translate(nodes);
            fragments.add(new ProcFrag(
                    currentFrame.procEntryExit1(currentTree.asStm()),
                    currentFrame
            ));
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
                Booleans.asList(new boolean[currentMethod.getParameters().size() + 1])
        );

        final List<Node> nodes = new LinkedList<>();
        nodes.addAll(declaration.getFormals());
        nodes.addAll(declaration.getLocals());
        nodes.addAll(declaration.getStatements());

        final MOVE returnStatement = new MOVE(
            new TEMP(currentFrame.RV()),
            translate(declaration.getReturnExpression()).asExp());

        if (nodes.isEmpty()) {
            currentTree = new Statement(returnStatement);
        } else {
            currentTree = new Statement(new SEQ(
                translate(nodes).asStm(),
                returnStatement
            ));
        }

        fragments.add(new ProcFrag(
                currentFrame.procEntryExit1(currentTree.asStm()),
                currentFrame));

        currentFrame = null;
        currentMethod.leaveBlock();
        currentMethod = null;
    }

    @Override
    public void caseAFieldDeclaration(final AFieldDeclaration declaration) {
        currentTree = null;
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

        final List<Node> nodes = new LinkedList<>();
        nodes.addAll(block.getLocals());
        nodes.addAll(block.getStatements());
        Translation tree = translate(nodes);

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
            translate(statement.getCondition()),
            translate(statement.getStatement())
        );
    }

    @Override
    public void caseAIfElseStatement(final AIfElseStatement statement) {
        currentTree = new IfElse(
            translate(statement.getCondition()),
            translate(statement.getThen()),
            translate(statement.getElse())
        );
    }

    @Override
    public void caseAWhileStatement(final AWhileStatement statement) {
        final Label test = new Label();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        currentTree = new Statement(
            new SEQ(new LABEL(test),
            new SEQ(translate(statement.getCondition()).asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(trueLabel),
            new SEQ(translate(statement.getStatement()).asStm(),
                    new LABEL(falseLabel))))));
    }

    @Override
    public void caseAPrintlnStatement(final APrintlnStatement statement) {
        currentTree = new Expression(
            currentFrame.externalCall("_minijavalib_println",
            new ExpList(translate(statement.getValue()).asExp())));
    }

    @Override
    public void caseAAssignStatement(final AAssignStatement statement) {
        currentTree = new Statement(
            new MOVE(translate(statement.getName()), translate(statement.getValue()).asExp()));
    }

    @Override
    public void caseAArrayAssignStatement(final AArrayAssignStatement statement) {
        currentTree = new Statement(
            new MOVE(new BINOP(
                    BINOP.PLUS,
                    translate(statement.getName()),
                    new BINOP(
                        BINOP.MUL,
                        translate(statement.getIndex()).asExp(),
                        new CONST(currentFrame.wordSize())
                    )
                ),
                translate(statement.getValue()).asExp())
            );
    }

    @Override
    public void caseAAndExpression(final AAndExpression expression) {
        final Temp result = new Temp();
        final Label testRight = new Label();
        final Label setTrue = new Label();
        final Label finished = new Label();

        currentTree = new Expression(new ESEQ(
            new SEQ(new MOVE(new TEMP(result), new CONST(0)),
            new SEQ(translate(expression.getLeft()).asCond(testRight, finished),
            new SEQ(new LABEL(testRight),
            new SEQ(translate(expression.getRight()).asCond(setTrue, finished),
            new SEQ(new LABEL(setTrue),
            new SEQ(new MOVE(new TEMP(result), new CONST(1)),
                    new LABEL(finished))))))),
                    new TEMP(result)
        ));
    }

    @Override
    public void caseAOrExpression(final AOrExpression expression) {
        final Temp result = new Temp();
        final Label testRight = new Label();
        final Label setFalse = new Label();
        final Label finished = new Label();

        currentTree = new Expression(new ESEQ(
            new SEQ(new MOVE(new TEMP(result), new CONST(1)),
            new SEQ(translate(expression.getLeft()).asCond(finished, testRight),
            new SEQ(new LABEL(testRight),
            new SEQ(translate(expression.getRight()).asCond(finished, setFalse),
            new SEQ(new LABEL(setFalse),
            new SEQ(new MOVE(new TEMP(result), new CONST(0)),
                    new LABEL(finished))))))),
                    new TEMP(result)
        ));
    }

    @Override
    public void caseALessThanExpression(final ALessThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.LT,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseAGreaterThanExpression(final AGreaterThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.GT,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseAGreaterEqualThanExpression(final AGreaterEqualThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.GE,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseALessEqualThanExpression(final ALessEqualThanExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.LE,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseAEqualExpression(final AEqualExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.EQ,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseANotEqualExpression(final ANotEqualExpression expression) {
        currentTree = new RelationalCondition(
            CJUMP.NE,
            translate(expression.getLeft()),
            translate(expression.getRight()));
    }

    @Override
    public void caseAPlusExpression(final APlusExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.PLUS,
            translate(expression.getLeft()).asExp(),
            translate(expression.getRight()).asExp()));
    }

    @Override
    public void caseAMinusExpression(final AMinusExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.MINUS,
            translate(expression.getLeft()).asExp(),
            translate(expression.getRight()).asExp()));
    }

    @Override
    public void caseATimesExpression(final ATimesExpression expression) {
        currentTree = new Expression(new BINOP(BINOP.MUL,
            translate(expression.getLeft()).asExp(),
            translate(expression.getRight()).asExp()));
    }

    @Override
    public void caseANotExpression(final ANotExpression expression) {
        currentTree = new Expression(new BINOP(
            BINOP.MINUS,
            new CONST(1),
            translate(expression.getExpression()).asExp()));
    }

    @Override
    public void caseAMethodInvocationExpression(final AMethodInvocationExpression expression) {
        ExpList args = new ExpList(translate(expression.getInstance()).asExp());
        ExpList current = args;
        for (PExpression arg : expression.getActuals()) {
            current.tail = new ExpList(translate(arg).asExp());
            current = current.tail;
        }

        final String className = types.get(expression.getInstance()).getName();
        final String methodName = expression.getName().getText();

        currentTree = new Expression(
            new CALL(
                new NAME(new Label(className + '$' + methodName)),
                args)
            );
    }

    @Override
    public void caseAArrayAccessExpression(final AArrayAccessExpression expression) {
        currentTree = new Expression(new MEM(
            new BINOP(BINOP.PLUS,
                translate(expression.getArray()).asExp(),
                new BINOP(BINOP.MUL,
                    translate(expression.getIndex()).asExp(),
                    new CONST(currentFrame.wordSize())
                )
            )
        ));
    }

    @Override
    public void caseAArrayLengthExpression(final AArrayLengthExpression expression) {
        currentTree = new Expression(new MEM(
            new BINOP(BINOP.MINUS,
                translate(expression.getArray()).asExp(),
                new CONST(currentFrame.wordSize())
            )
        ));
    }

    @Override
    public void caseANewInstanceExpression(final ANewInstanceExpression expression) {
        currentTree = new Expression(
            currentFrame.externalCall(
                "_minijavalib_allocate",
                new ExpList(new CONST(
                    symbolTable.getClassInfo(
                        expression.getClassName().getText()).getNumFields() * currentFrame.wordSize()
                    )
                )
            )
        );
    }

    @Override
    public void caseANewIntArrayExpression(final ANewIntArrayExpression expression) {
        currentTree = new Expression(
            currentFrame.externalCall(
                "_minijavalib_initarray",
                new ExpList(translate(expression.getSize()).asExp())
            )
        );
    }

    @Override
    public void caseAIntegerExpression(final AIntegerExpression expression) {
        currentTree = new Expression(
            new CONST(Integer.parseInt(expression.getInteger().getText())));
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
        currentTree = new Expression(translate(expression.getIdentifier()));
    }

    @Override
    public void caseAThisExpression(final AThisExpression expression) {
        currentTree = new Expression(currentFrame.formals().get(0).exp(new TEMP(currentFrame.FP())));
    }
}
