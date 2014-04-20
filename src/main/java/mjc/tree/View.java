package mjc.tree;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * A class that creates a tree view of Appel's TREE code. Can be used as follows:
 *
 * <PRE>
 *     tree.View viewer = new tree.View(&quot;Name of function&quot;);
 *     viewer.addStm(treeBody);
 *     viewer.expandTree();
 * </PRE>
 */
public class View {

    private DefaultMutableTreeNode top;
    private JTree tree;

    public View(String title) {
        top = new DefaultMutableTreeNode(title);
        tree = new JTree(top);
        JScrollPane pane = new JScrollPane(tree);
        JFrame frame = new JFrame("TREE Code Viewer " + title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);
        frame.getContentPane().add(pane);
        frame.setVisible(true);
    }

    public void addStm(Stm s) {
        addStm(s, top, true);
    }

    public void addExp(Exp e) {
        addExp(e, top, true);
    }

    public void expandTree() {
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    void addStm(SEQ s, DefaultMutableTreeNode parent, boolean createNew) {
        if (createNew) {
            DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("SEQ");
            addStm(s.left, thisNode, true);
            addStm(s.right, thisNode, false);
            parent.add(thisNode);
        } else {
            addStm(s.left, parent, true);
            addStm(s.right, parent, false);
        }
    }

    void addStm(LABEL s, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("LABEL");
        thisNode.add(new DefaultMutableTreeNode(s.label.toString()));
        parent.add(thisNode);
    }

    void addStm(JUMP s, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("JUMP");
        addExp(s.expression, thisNode, true);
        parent.add(thisNode);
    }

    void addStm(CJUMP s, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("CJUMP");
        switch (s.op) {
            case CJUMP.EQ:
                thisNode.add(new DefaultMutableTreeNode("EQ"));
                break;
            case CJUMP.NE:
                thisNode.add(new DefaultMutableTreeNode("NE"));
                break;
            case CJUMP.LT:
                thisNode.add(new DefaultMutableTreeNode("LT"));
                break;
            case CJUMP.GT:
                thisNode.add(new DefaultMutableTreeNode("GT"));
                break;
            case CJUMP.LE:
                thisNode.add(new DefaultMutableTreeNode("LE"));
                break;
            case CJUMP.GE:
                thisNode.add(new DefaultMutableTreeNode("GE"));
                break;
            case CJUMP.ULT:
                thisNode.add(new DefaultMutableTreeNode("ULT"));
                break;
            case CJUMP.ULE:
                thisNode.add(new DefaultMutableTreeNode("ULE"));
                break;
            case CJUMP.UGT:
                thisNode.add(new DefaultMutableTreeNode("UGT"));
                break;
            case CJUMP.UGE:
                thisNode.add(new DefaultMutableTreeNode("UGE"));
                break;
            default:
                throw new Error("Unknown CJUMP operator " + s.op);
        }
        addExp(s.left, thisNode, true);
        addExp(s.right, thisNode, true);
        thisNode.add(new DefaultMutableTreeNode(s.trueLabel.toString()));
        thisNode.add(new DefaultMutableTreeNode(s.falseLabel.toString()));
        parent.add(thisNode);
    }

    void addStm(MOVE s, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("MOVE");
        addExp(s.destination, thisNode, true);
        addExp(s.source, thisNode, true);
        parent.add(thisNode);
    }

    void addStm(EXP s, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("EXP");
        addExp(s.expression, thisNode, true);
        parent.add(thisNode);
    }

    void addStm(Stm s, DefaultMutableTreeNode parent, boolean createNew) {
        if (s instanceof SEQ)
            addStm((SEQ) s, parent, createNew);
        else if (s instanceof LABEL)
            addStm((LABEL) s, parent);
        else if (s instanceof JUMP)
            addStm((JUMP) s, parent);
        else if (s instanceof CJUMP)
            addStm((CJUMP) s, parent);
        else if (s instanceof MOVE)
            addStm((MOVE) s, parent);
        else if (s instanceof EXP)
            addStm((EXP) s, parent);
        else if (s == null)
            throw new Error("s == null");
        else
            throw new Error("Unknown Stm `" + s.getClass().getName() + "`");
    }

    void addExp(BINOP e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("BINOP");
        switch (e.op) {
            case BINOP.PLUS:
                thisNode.add(new DefaultMutableTreeNode("PLUS"));
                break;
            case BINOP.MINUS:
                thisNode.add(new DefaultMutableTreeNode("MINUS"));
                break;
            case BINOP.MUL:
                thisNode.add(new DefaultMutableTreeNode("MUL"));
                break;
            case BINOP.DIV:
                thisNode.add(new DefaultMutableTreeNode("DIV"));
                break;
            case BINOP.AND:
                thisNode.add(new DefaultMutableTreeNode("AND"));
                break;
            case BINOP.OR:
                thisNode.add(new DefaultMutableTreeNode("OR"));
                break;
            case BINOP.LSHIFT:
                thisNode.add(new DefaultMutableTreeNode("LSHIFT"));
                break;
            case BINOP.RSHIFT:
                thisNode.add(new DefaultMutableTreeNode("RSHIFT"));
                break;
            case BINOP.ARSHIFT:
                thisNode.add(new DefaultMutableTreeNode("ARSHIFT"));
                break;
            case BINOP.XOR:
                thisNode.add(new DefaultMutableTreeNode("XOR"));
                break;
            default:
                throw new Error("Unknown BINOP operator " + e.op);
        }
        addExp(e.left, thisNode, true);
        addExp(e.right, thisNode, true);
        parent.add(thisNode);
    }

    void addExp(MEM e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("MEM");
        addExp(e.expression, thisNode, true);
        parent.add(thisNode);
    }

    void addExp(TEMP e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("TEMP");
        thisNode.add(new DefaultMutableTreeNode(e.temp.toString()));
        parent.add(thisNode);
    }

    void addExp(ESEQ e, DefaultMutableTreeNode parent, boolean createNew) {
        if (createNew) {
            DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("ESEQ");
            addStm(e.statement, thisNode, true);
            addExp(e.expression, thisNode, false);
            parent.add(thisNode);
        } else {
            addStm(e.statement, parent, true);
            addExp(e.expression, parent, false);
        }
    }

    void addExp(NAME e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("NAME");
        thisNode.add(new DefaultMutableTreeNode(e.label.toString()));
        parent.add(thisNode);
    }

    void addExp(CONST e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("CONST");
        thisNode.add(new DefaultMutableTreeNode(String.valueOf(e.value)));
        parent.add(thisNode);
    }

    void addExp(CALL e, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode("CALL");
        addExp(e.function, thisNode, true);
        for (ExpList a = e.arguments; a != null; a = a.tail) {
            addExp(a.head, thisNode, true);
        }
        parent.add(thisNode);
    }

    void addExp(Exp e, DefaultMutableTreeNode parent, boolean createNew) {
        if (e instanceof BINOP)
            addExp((BINOP) e, parent);
        else if (e instanceof MEM)
            addExp((MEM) e, parent);
        else if (e instanceof TEMP)
            addExp((TEMP) e, parent);
        else if (e instanceof ESEQ)
            addExp((ESEQ) e, parent, createNew);
        else if (e instanceof NAME)
            addExp((NAME) e, parent);
        else if (e instanceof CONST)
            addExp((CONST) e, parent);
        else if (e instanceof CALL)
            addExp((CALL) e, parent);
        else if (e == null)
            throw new Error("e == null");
        else
            throw new Error("Unknown Exp `" + e.getClass().getName() + "`");
    }
}
