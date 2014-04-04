package mjc.tree;

import mjc.temp.Label;

public class CJUMP extends Stm {
    public final int op;
    public final Exp left;
    public final Exp right;
    public final Label trueLabel;
    public final Label falseLabel;

    public final static int
        EQ = 0,
        NE = 1,
        LT = 2,
        GT = 3,
        LE = 4,
        GE = 5,
        ULT = 6,
        ULE = 7,
        UGT = 8,
        UGE = 9;

    public CJUMP(final int op, final Exp left, final Exp right,
            final Label trueLabel, final Label falseLabel) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;
    }

    public ExpList kids() {
        return new ExpList(left, new ExpList(right));
    }

    public Stm build(final ExpList kids) {
        return new CJUMP(op, kids.head, kids.tail.head, trueLabel, falseLabel);
    }

    public static int notRel(int op) {
        switch (op) {
            case EQ:
                return NE;
            case NE:
                return EQ;
            case LT:
                return GE;
            case GE:
                return LT;
            case GT:
                return LE;
            case LE:
                return GT;
            case ULT:
                return UGE;
            case UGE:
                return ULT;
            case UGT:
                return ULE;
            case ULE:
                return UGT;
            default:
                throw new Error("bad relop in CJUMP.notRel");
        }
    }
}
