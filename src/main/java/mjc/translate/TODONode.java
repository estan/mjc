package mjc.translate;

import mjc.temp.Label;
import mjc.tree.CONST;
import mjc.tree.Exp;
import mjc.tree.LABEL;
import mjc.tree.Stm;

public class TODONode extends TreeNode {
    @Override
    Exp asExp() {
        return new CONST(42);
    }

    @Override
    Stm asStm() {
        return new LABEL(new Label("TODO"));
    }

    @Override
    Stm asCond(Label trueLabel, Label falseLabel) {
        return new LABEL(new Label("TODO"));
    }
}
