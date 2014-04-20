class NotEqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //if (i != i) {}   // OK!
        if (i != ia) {}   // INVALID_BINARY_OP
        if (i != b) {}   // INVALID_BINARY_OP
        if (i != cA) {}   // INVALID_BINARY_OP
        if (i != cB) {}   // INVALID_BINARY_OP

        if (ia != i) {}   // INVALID_BINARY_OP
        //if (ia != ia) {}   // OK!
        if (ia != b) {}   // INVALID_BINARY_OP
        if (ia != cA) {}   // INVALID_BINARY_OP
        if (ia != cB) {}   // INVALID_BINARY_OP

        if (b != i) {}   // INVALID_BINARY_OP
        if (b != ia) {}   // INVALID_BINARY_OP
        //if (b != b) {}   // OK!
        if (b != cA) {}   // INVALID_BINARY_OP
        if (b != cB) {}   // INVALID_BINARY_OP

        if (cA != i) {}   // INVALID_BINARY_OP
        if (cA != ia) {}   // INVALID_BINARY_OP
        if (cA != b) {}   // INVALID_BINARY_OP
        //if (cA != cA) {}   // OK!
        if (cA != cB) {}   // INVALID_BINARY_OP

        if (cB != i) {}   // INVALID_BINARY_OP
        if (cB != ia) {}   // INVALID_BINARY_OP
        if (cB != b) {}   // INVALID_BINARY_OP
        if (cB != cA) {}   // INVALID_BINARY_OP
        //if (cB != cB) {}   // OK!
    }
}
class A {}
class B {}
