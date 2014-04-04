package mjc.tree;

public class EXP extends Stm {
    public final Exp expression;

    public EXP(final Exp expression) {
        this.expression = expression;
    }

    public ExpList kids() {
        return new ExpList(expression);
    }

    public Stm build(final ExpList kids) {
        return new EXP(kids.head);
    }
}
