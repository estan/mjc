package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CJUMP;
import mjc.tree.Stm;

public class RelCond extends CondNode {
    int op;
    private final TreeNode left;
    private final TreeNode right;

    RelCond(int op, TreeNode left, TreeNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    Stm asCond(Label trueLabel, Label falseLabel) {
        return new CJUMP(op, left.asExp(), right.asExp(), trueLabel, falseLabel);
    }
}
