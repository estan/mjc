package mjc.asm;

public class InstrList {
    public final Instr head;
    public final InstrList tail;

    public InstrList(final Instr head, final InstrList tail) {
        this.head = head;
        this.tail = tail;
    }
}
