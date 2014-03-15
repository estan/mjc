class AndExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;
        if (i && b) {} // INVALID_BINARY_OP
        if (ia && b) {} // INVALID_BINARY_OP
        if (l && b) {} // INVALID_BINARY_OP
        if (la && b) {} // INVALID_BINARY_OP
        if (cA && b) {} // INVALID_BINARY_OP
        if (b && i) {} // INVALID_BINARY_OP
        if (b && ia) {} // INVALID_BINARY_OP
        if (b && l) {} // INVALID_BINARY_OP
        if (b && la) {} // INVALID_BINARY_OP
        if (b && cA) {} // INVALID_BINARY_OP

        // We could test all combinations, but lets hope this suffices.
    }
}
class A {}
class B {}
