package mjc.tree;

import mjc.temp.Label;

public class NAME extends Exp {
    public final Label label;

    public NAME(Label label) {
        this.label = label;
    }

    public ExpList kids() {
        return null;
    }

    public Exp build(final ExpList kids) {
        return this;
    }
}
