package mjc.tree;

public class SEQ extends Stm {
    public final Stm left;
    public final Stm right;

    public SEQ(final Stm left, final Stm right) {
        this.left = left;
        this.right = right;
    }

    public ExpList kids() {
        throw new Error("kids() not applicable to SEQ");
    }

    public Stm build(final ExpList kids) {
        throw new Error("build() not applicable to SEQ");
    }
}
