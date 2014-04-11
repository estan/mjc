package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.EXP;
import mjc.tree.Exp;
import mjc.tree.Stm;

/**
 * Expression represents the translation of an expression.
 */
class Expression implements Translation {
    final Exp expression;

    Expression(Exp expression) {
        this.expression = expression;
    }

    @Override
    public Exp asExp() {
        return expression;
    }

    @Override
    public Stm asStm() {
        return new EXP(asExp());
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        return new CJUMP(CJUMP.NE, asExp(), new CONST(0), trueLabel, falseLabel);
    }
}
