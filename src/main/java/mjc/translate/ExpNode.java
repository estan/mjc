package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.EXP;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * ExpNode represents the IR translation of an expression.
 */
class ExpNode extends TreeNode {
    final Exp expression;

    ExpNode(Exp expression) {
        this.expression = expression;
    }

    @Override
    Exp asExp() {
        return expression;
    }

    @Override
    Stm asStm() {
        // Apply EXP to evaluate expression and discard the result.
        return new EXP(asExp());
    }

    @Override
    Stm asCond(Label trueLabel, Label falseLabel) {
        // Jump to trueLabel or falseLabel based on evaluation of expression.
        return new CJUMP(CJUMP.NE, asExp(), new CONST(0), trueLabel, falseLabel);
    }

}
