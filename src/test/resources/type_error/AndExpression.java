class AndExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;
        if (i && b) {} // INVALID_LEFT_OP_AND
        if (ia && b) {} // INVALID_LEFT_OP_AND
        if (l && b) {} // INVALID_LEFT_OP_AND
        if (la && b) {} // INVALID_LEFT_OP_AND
        if (cA && b) {} // INVALID_LEFT_OP_AND
        if (b && i) {} // INVALID_RIGHT_OP_AND
        if (b && ia) {} // INVALID_RIGHT_OP_AND
        if (b && l) {} // INVALID_RIGHT_OP_AND
        if (b && la) {} // INVALID_RIGHT_OP_AND
        if (b && cA) {} // INVALID_RIGHT_OP_AND

        // We could test all combinations, but lets hope this suffices.
    }
}
class A {}
class B {}
