package mjc.temp;

public class TempList {
    public final Temp head;
    public final TempList tail;

    public TempList(final Temp head, final TempList tail) {
        this.head = head;
        this.tail = tail;
    }
}
