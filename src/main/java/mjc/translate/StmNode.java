package mjc.translate;

import mjc.temp.Label;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * StmNode represents the IR translation of a statement.
 */
class StmNode extends TreeNode {
    final Stm statement;

    StmNode(Stm statement) {
        this.statement = statement;
    }

    @Override
    Exp asExp() {
        throw new Error("StmNode has no expression representation");
    }

    @Override
    Stm asStm() {
        return statement;
    }

    @Override
    Stm asCond(Label trueLabel, Label falseLabel) {
        throw new Error("StmNode has no conditional representation");
    }
}
