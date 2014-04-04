package mjc.tree;

public class MOVE extends Stm {
    public final Exp destination;
    public final Exp source;

    public MOVE(final Exp destination, final Exp source) {
        this.destination = destination;
        this.source = source;
    }

    public ExpList kids() {
        if (destination instanceof MEM) {
            return new ExpList(((MEM) destination).expression, source);
        } else {
            return new ExpList(source);
        }
    }

    public Stm build(final ExpList kids) {
        if (destination instanceof MEM) {
            return new MOVE(new MEM(kids.head), kids.tail.head);
        } else {
            return new MOVE(destination, kids.head);
        }
    }
}
