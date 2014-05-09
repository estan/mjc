package mjc.tree;

public class ExpList {
    public Exp head;
    public ExpList tail;

    public ExpList(final Exp head, final ExpList tail) {
        this.head = head;
        this.tail = tail;
    }

    public ExpList(final Exp head) {
        this.head = head;
        tail = null;
    }

    public ExpList(final Exp e1, final Exp e2) {
        head = e1;
        tail = new ExpList(e2);
    }
}
