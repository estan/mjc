package mjc.tree;

public class CALL extends Exp {
    public final Exp function;
    public final ExpList arguments;

    public CALL(final Exp function, final ExpList arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public ExpList kids() {
        return new ExpList(function, arguments);
    }

    public Exp build(final ExpList kids) {
        return new CALL(kids.head, kids.tail);
    }

}
