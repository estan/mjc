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
 * Condition represents the translation of a conditional expression.
 *
 * Implementations of {@link #asExp()} and {@link #asStm()} are provided, while
 * subclasses are left to implement {@link #asCond(Label, Label)} on their own.
 */
abstract class Condition implements Translation {

    @Override
    public Exp asExp() {
        final Temp result = new Temp();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        return new ESEQ(
            new SEQ(new MOVE(new TEMP(result), new CONST(1)),
            new SEQ(asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(falseLabel),
            new SEQ(new MOVE(new TEMP(result), new CONST(0)),
                    new LABEL(trueLabel))))),
            new TEMP(result));
    }

    @Override
    public Stm asStm() {
        return new EXP(asExp());
    }

    @Override
    public abstract Stm asCond(Label trueLabel, Label falseLabel);
}
