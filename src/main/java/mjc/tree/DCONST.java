package mjc.tree;

public class DCONST extends Exp {
    public long value;

    public DCONST(long value) {
        this.value = value;
    }

    public ExpList kids() {
        return null;
    }

    public Exp build(final ExpList kids) {
        return this;
    }
}
