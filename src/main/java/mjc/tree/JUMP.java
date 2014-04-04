package mjc.tree;

import mjc.temp.Label;
import mjc.temp.LabelList;

public class JUMP extends Stm {
    public Exp expression;
    public LabelList targets;

    public JUMP(final Exp expression, final LabelList targets) {
        this.expression = expression;
        this.targets = targets;
    }

    public JUMP(final Label target) {
        this(new NAME(target), new LabelList(target));
    }

    public ExpList kids() {
        return new ExpList(expression);
    }

    public Stm build(final ExpList kids) {
        return new JUMP(kids.head, targets);
    }
}
