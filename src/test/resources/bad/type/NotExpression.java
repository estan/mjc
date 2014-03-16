class NotExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        if (!i) {} // INVALID_UNARY_OP
        if (!ia) {} // INVALID_UNARY_OP
        if (!l) {} // INVALID_UNARY_OP
        if (!la) {} // INVALID_UNARY_OP
        //if (!b) {} // OK!
        if (!cA) {} // INVALID_UNARY_OP
    }
}
class A {}
class B {}
