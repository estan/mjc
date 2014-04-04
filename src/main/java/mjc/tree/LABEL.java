package mjc.tree;

import mjc.temp.Label;

public class LABEL extends Stm {
    public final Label label;

    public LABEL(final Label label) {
        this.label = label;
    }

    public ExpList kids() {
        return null;
    }

    public Stm build(final ExpList kids) {
        return this;
    }
}
