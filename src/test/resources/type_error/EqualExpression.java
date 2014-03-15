class EqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        if (i == ia) {}  // INVALID_BINARY_OP
        if (i == la) {}  // INVALID_BINARY_OP
        if (i == b) {}   // INVALID_BINARY_OP
        if (i == cA) {}  // INVALID_BINARY_OP

        if (ia == i) {}  // INVALID_BINARY_OP
        if (ia == l) {}  // INVALID_BINARY_OP
        if (ia == la) {} // INVALID_BINARY_OP
        if (ia == b) {}  // INVALID_BINARY_OP
        if (ia == cA) {} // INVALID_BINARY_OP

        if (l == ia) {}  // INVALID_BINARY_OP
        if (l == la) {}  // INVALID_BINARY_OP
        if (l == b) {}   // INVALID_BINARY_OP
        if (l == cA) {}  // INVALID_BINARY_OP

        if (la == i) {}  // INVALID_BINARY_OP
        if (la == ia) {} // INVALID_BINARY_OP
        if (la == l) {}  // INVALID_BINARY_OP
        if (la == b) {}  // INVALID_BINARY_OP
        if (la == cA) {} // INVALID_BINARY_OP

        if (b == i) {}   // INVALID_BINARY_OP
        if (b == ia) {}  // INVALID_BINARY_OP
        if (b == l) {}   // INVALID_BINARY_OP
        if (b == la) {}  // INVALID_BINARY_OP
        if (b == cA) {}  // INVALID_BINARY_OP

        if (cA == i) {}  // INVALID_BINARY_OP
        if (cA == ia) {} // INVALID_BINARY_OP
        if (cA == l) {}  // INVALID_BINARY_OP
        if (cA == la) {} // INVALID_BINARY_OP
        if (cA == b) {}  // INVALID_BINARY_OP
        if (cA == cB) {} // INVALID_BINARY_OP
    }
}
class A {}
class B {}
