package mjc.tree;

public class CALL extends Exp {
    public final Exp method;
    public final ExpList arguments;

    public CALL(final Exp method, final ExpList arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public ExpList kids() {
        return new ExpList(method, arguments);
    }

    public Exp build(final ExpList kids) {
        return new CALL(kids.head, kids.tail);
    }

}
