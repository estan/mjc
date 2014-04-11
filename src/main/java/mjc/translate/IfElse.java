package mjc.translate;

import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.tree.CJUMP;
import mjc.tree.CONST;
import mjc.tree.ESEQ;
import mjc.tree.Exp;
import mjc.tree.JUMP;
import mjc.tree.LABEL;
import mjc.tree.MOVE;
import mjc.tree.SEQ;
import mjc.tree.Stm;
import mjc.tree.TEMP;

public class IfElse implements Translation {
    final Translation condition;
    final Translation thenBody;
    final Translation elseBody;

    IfElse(Translation condition, Translation thenBody, Translation elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public Exp asExp() {
        final Temp result = new Temp();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();
        final Label joinLabel = new Label();

        return new ESEQ(
            new SEQ(condition.asCond(trueLabel, falseLabel),
            new SEQ(new LABEL(trueLabel),
            new SEQ(new MOVE(new TEMP(result), thenBody.asExp()),
            new SEQ(new JUMP(joinLabel),
            new SEQ(new LABEL(falseLabel),
            new SEQ(new MOVE(new TEMP(result), elseBody.asExp()),
                    new LABEL(joinLabel))))))),
            new TEMP(result));
    }

    @Override
    public Stm asStm() {
        final Label joinLabel = new Label();
        final Label trueLabel = new Label();
        final Label falseLabel = new Label();

        return new SEQ(condition.asCond(trueLabel, falseLabel),
               new SEQ(new LABEL(trueLabel),
               new SEQ(thenBody.asStm(),
               new SEQ(new JUMP(joinLabel),
               new SEQ(new LABEL(falseLabel),
               new SEQ(elseBody.asStm(),
                       new LABEL(joinLabel)))))));
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        return new CJUMP(CJUMP.NE, asExp(), new CONST(0), trueLabel, falseLabel);
    }
}
