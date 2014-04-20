class TimesExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //i = i * i; // OK!
        i = i * ia; // INVALID_BINARY_OP
        i = i * b; // INVALID_BINARY_OP
        i = i * cA; // INVALID_BINARY_OP
        i = i * cB; // INVALID_BINARY_OP

        i = ia * i; // INVALID_BINARY_OP
        i = ia * ia; // INVALID_BINARY_OP
        i = ia * b; // INVALID_BINARY_OP
        i = ia * cA; // INVALID_BINARY_OP
        i = ia * cB; // INVALID_BINARY_OP

        i = b * i; // INVALID_BINARY_OP
        i = b * ia; // INVALID_BINARY_OP
        i = b * b; // INVALID_BINARY_OP
        i = b * cA; // INVALID_BINARY_OP
        i = b * cB; // INVALID_BINARY_OP

        i = cA * i; // INVALID_BINARY_OP
        i = cA * ia; // INVALID_BINARY_OP
        i = cA * b; // INVALID_BINARY_OP
        i = cA * cA; // INVALID_BINARY_OP
        i = cA * cB; // INVALID_BINARY_OP

        i = cB * i; // INVALID_BINARY_OP
        i = cB * ia; // INVALID_BINARY_OP
        i = cB * b; // INVALID_BINARY_OP
        i = cB * cA; // INVALID_BINARY_OP
        i = cB * cB; // INVALID_BINARY_OP

    }
}
class A {}
class B {}
