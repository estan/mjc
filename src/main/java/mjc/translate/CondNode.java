package mjc.translate;

import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.tree.CONST;
import mjc.tree.ESEQ;
import mjc.tree.EXP;
import mjc.tree.Exp;
import mjc.tree.LABEL;
import mjc.tree.MOVE;
import mjc.tree.SEQ;
import mjc.tree.Stm;
import mjc.tree.TEMP;

/**
 * Abstract base class for conditional nodes.
 */
abstract class CondNode extends TreeNode {

    @Override
    Exp asExp() {
        final Temp result = new Temp();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        /*
         * result = 1
         * if (!cond)
         *     result = 0
         * return result
         */
        return new ESEQ(
            new SEQ(new MOVE(new TEMP(result), new CONST(1)),
            new SEQ(asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(falseLabel),
            new SEQ(new MOVE(new TEMP(result), new CONST(0)),
                    new LABEL(trueLabel))))),
            new TEMP(result));
    }

    @Override
    Stm asStm() {
        // Apply EXP to evaluate expression and discard the result.
        return new EXP(asExp());
    }

    abstract Stm asCond(Label trueLabel, Label falseLabel);
}
