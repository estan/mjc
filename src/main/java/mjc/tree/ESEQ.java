package mjc.tree;

public class ESEQ extends Exp {
    public final Stm statement;
    public final Exp expression;

    public ESEQ(final Stm statement, final Exp expression) {
        this.statement = statement;
        this.expression = expression;
    }

    public ExpList kids() {
        throw new Error("kids() not applicable to ESEQ");
    }

    public Exp build(ExpList kids) {
        throw new Error("build() not applicable to ESEQ");
    }
}
