class GreaterEqualThanExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //if (i >= i) {} // OK!
        if (i >= ia) {} // INVALID_BINARY_OP
        if (i >= b) {} // INVALID_BINARY_OP
        if (i >= cA) {} // INVALID_BINARY_OP
        if (i >= cB) {} // INVALID_BINARY_OP

        if (ia >= i) {} // INVALID_BINARY_OP
        if (ia >= ia) {} // INVALID_BINARY_OP
        if (ia >= b) {} // INVALID_BINARY_OP
        if (ia >= cA) {} // INVALID_BINARY_OP
        if (ia >= cB) {} // INVALID_BINARY_OP

        if (b >= i) {} // INVALID_BINARY_OP
        if (b >= ia) {} // INVALID_BINARY_OP
        if (b >= b) {} // INVALID_BINARY_OP
        if (b >= cA) {} // INVALID_BINARY_OP
        if (b >= cB) {} // INVALID_BINARY_OP

        if (cA >= i) {} // INVALID_BINARY_OP
        if (cA >= ia) {} // INVALID_BINARY_OP
        if (cA >= b) {} // INVALID_BINARY_OP
        if (cA >= cA) {} // INVALID_BINARY_OP
        if (cA >= cB) {} // INVALID_BINARY_OP

        if (cB >= i) {} // INVALID_BINARY_OP
        if (cB >= ia) {} // INVALID_BINARY_OP
        if (cB >= b) {} // INVALID_BINARY_OP
        if (cB >= cA) {} // INVALID_BINARY_OP
        if (cB >= cB) {} // INVALID_BINARY_OP
    }
}
class A {}
class B {}
