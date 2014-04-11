package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CONST;
import mjc.tree.Exp;
import mjc.tree.LABEL;
import mjc.tree.Stm;

/**
 * This is just a temporary class we use for constructs we have not translated yet.
 */
public class TODO implements Translation {
    @Override
    public Exp asExp() {
        return new CONST(42);
    }

    @Override
    public Stm asStm() {
        return new LABEL(new Label("TODO"));
    }

    @Override
    public Stm asCond(Label trueLabel, Label falseLabel) {
        return new LABEL(new Label("TODO"));
    }
}
