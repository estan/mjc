package mjc.tree;

public class MEM extends Exp {
    public Exp expression;

    public MEM(final Exp expression) {
        this.expression = expression;
    }

    public ExpList kids() {
        return new ExpList(expression);
    }

    public Exp build(final ExpList kids) {
        return new MEM(kids.head);
    }
}
