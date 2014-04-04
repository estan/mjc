package mjc.tree;

public class CONST extends Exp {
    public int value;

    public CONST(int value) {
        this.value = value;
    }

    public ExpList kids() {
        return null;
    }

    public Exp build(final ExpList kids) {
        return this;
    }
}
