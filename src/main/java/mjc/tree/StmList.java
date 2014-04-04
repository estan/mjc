package mjc.tree;

public class StmList {
    public final Stm head;
    public final StmList tail;

    public StmList(final Stm head, final StmList tail) {
        this.head = head;
        this.tail = tail;
    }
}
