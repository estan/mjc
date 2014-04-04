package mjc.tree;

import mjc.temp.Temp;

public class TEMP extends Exp {
    public final Temp temp;

    public TEMP(final Temp temp) {
        this.temp = temp;
    }

    public ExpList kids() {
        return null;
    }

    public Exp build(final ExpList kids) {
        return this;
    }
}
