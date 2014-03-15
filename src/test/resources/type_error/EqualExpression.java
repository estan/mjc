class EqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        if (i == ia) {}  // INVALID_EQ_COMPARISON
        if (i == la) {}  // INVALID_EQ_COMPARISON
        if (i == b) {}   // INVALID_EQ_COMPARISON
        if (i == cA) {}  // INVALID_EQ_COMPARISON

        if (ia == i) {}  // INVALID_EQ_COMPARISON
        if (ia == l) {}  // INVALID_EQ_COMPARISON
        if (ia == la) {} // INVALID_EQ_COMPARISON
        if (ia == b) {}  // INVALID_EQ_COMPARISON
        if (ia == cA) {} // INVALID_EQ_COMPARISON

        if (l == ia) {}  // INVALID_EQ_COMPARISON
        if (l == la) {}  // INVALID_EQ_COMPARISON
        if (l == b) {}   // INVALID_EQ_COMPARISON
        if (l == cA) {}  // INVALID_EQ_COMPARISON

        if (la == i) {}  // INVALID_EQ_COMPARISON
        if (la == ia) {} // INVALID_EQ_COMPARISON
        if (la == l) {}  // INVALID_EQ_COMPARISON
        if (la == b) {}  // INVALID_EQ_COMPARISON
        if (la == cA) {} // INVALID_EQ_COMPARISON

        if (b == i) {}   // INVALID_EQ_COMPARISON
        if (b == ia) {}  // INVALID_EQ_COMPARISON
        if (b == l) {}   // INVALID_EQ_COMPARISON
        if (b == la) {}  // INVALID_EQ_COMPARISON
        if (b == cA) {}  // INVALID_EQ_COMPARISON

        if (cA == i) {}  // INVALID_EQ_COMPARISON
        if (cA == ia) {} // INVALID_EQ_COMPARISON
        if (cA == l) {}  // INVALID_EQ_COMPARISON
        if (cA == la) {} // INVALID_EQ_COMPARISON
        if (cA == b) {}  // INVALID_EQ_COMPARISON
        if (cA == cB) {} // INVALID_EQ_COMPARISON
    }
}
class A {}
class B {}
