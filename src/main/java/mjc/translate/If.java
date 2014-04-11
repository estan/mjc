package mjc.translate;

import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.ESEQ;
import mjc.tree.Exp;
import mjc.tree.LABEL;
import mjc.tree.MOVE;
import mjc.tree.SEQ;
import mjc.tree.Stm;
import mjc.tree.TEMP;

public class If implements Translation {
    final Translation condition;
    final Translation body;

    If(Translation condition, Translation body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Exp asExp() {
        final Temp result = new Temp();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        return new ESEQ(
            new SEQ(condition.asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(trueLabel),
            new SEQ(new MOVE(new TEMP(result), body.asExp()),
                    new LABEL(falseLabel)))),
            new TEMP(result));
    }

    @Override
    public Stm asStm() {
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        return new SEQ(condition.asCond(trueLabel, falseLabel),
               new SEQ(new LABEL(trueLabel),
               new SEQ(body.asStm(),
                       new LABEL(falseLabel))));
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        return new CJUMP(CJUMP.NE, asExp(), new CONST(0), trueLabel, falseLabel);
    }
}
