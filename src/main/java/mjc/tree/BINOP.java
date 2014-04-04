package mjc.tree;

public class BINOP extends Exp {

    public final static int
        PLUS = 0,
        MINUS = 1,
        MUL = 2,
        DIV = 3,
        AND = 4,
        OR = 5,
        LSHIFT = 6,
        RSHIFT = 7,
        ARSHIFT = 8,
        XOR = 9;

    public final int op;
    public final Exp left;
    public final Exp right;

    public BINOP(int op, final Exp left, final Exp right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public ExpList kids() {
        return new ExpList(left, new ExpList(right));
    }

    public Exp build(final ExpList kids) {
        return new BINOP(op, kids.head, kids.tail.head);
    }
}
