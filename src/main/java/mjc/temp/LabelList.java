package mjc.temp;

public class LabelList {
    public final Label head;
    public final LabelList tail;

    public LabelList(final Label head, LabelList tail) {
        this.head = head;
        this.tail = tail;
    }

    public LabelList(final Label head) {
        this.head = head;
        this.tail = null;
    }
}
