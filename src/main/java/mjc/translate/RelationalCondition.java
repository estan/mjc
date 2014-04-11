package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CJUMP;
import mjc.tree.Stm;

/**
 * RelationalCondition represents the translation of a relational expression.
 *
 * These are expressions like "x != 0" or "y < 5".
 */
public class RelationalCondition extends Condition {
    int op;
    private final Translation left;
    private final Translation right;

    /**
     * Constructs a RelationalCondition.
     *
     * @param op CJUMP operand.
     * @param left Left-hand side.
     * @param right Right-handl side.
     */
    RelationalCondition(int op, Translation left, Translation right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        return new CJUMP(op, left.asExp(), right.asExp(), trueLabel, falseLabel);
    }
}
