class MinusExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        //i = i - i; // OK!
        i = i - ia; // INVALID_BINARY_OP
        l = i - l; // INVALID_BINARY_OP
        i = i - la; // INVALID_BINARY_OP
        i = i - b; // INVALID_BINARY_OP
        i = i - cA; // INVALID_BINARY_OP
        i = i - cB; // INVALID_BINARY_OP

        i = ia - i; // INVALID_BINARY_OP
        i = ia - ia; // INVALID_BINARY_OP
        l = ia - l; // INVALID_BINARY_OP
        i = ia - la; // INVALID_BINARY_OP
        i = ia - b; // INVALID_BINARY_OP
        i = ia - cA; // INVALID_BINARY_OP
        i = ia - cB; // INVALID_BINARY_OP

        //l = l - i; // OK!
        l = l - ia; // INVALID_BINARY_OP
        //l = l - l; // OK!
        l = l - la; // INVALID_BINARY_OP
        l = l - b; // INVALID_BINARY_OP
        l = l - cA; // INVALID_BINARY_OP
        l = l - cB; // INVALID_BINARY_OP

        i = la - i; // INVALID_BINARY_OP
        i = la - ia; // INVALID_BINARY_OP
        l = la - l; // INVALID_BINARY_OP
        i = la - la; // INVALID_BINARY_OP
        i = la - b; // INVALID_BINARY_OP
        i = la - cA; // INVALID_BINARY_OP
        i = la - cB; // INVALID_BINARY_OP

        i = b - i; // INVALID_BINARY_OP
        i = b - ia; // INVALID_BINARY_OP
        l = b - l; // INVALID_BINARY_OP
        i = b - la; // INVALID_BINARY_OP
        i = b - b; // INVALID_BINARY_OP
        i = b - cA; // INVALID_BINARY_OP
        i = b - cB; // INVALID_BINARY_OP

        i = cA - i; // INVALID_BINARY_OP
        i = cA - ia; // INVALID_BINARY_OP
        l = cA - l; // INVALID_BINARY_OP
        i = cA - la; // INVALID_BINARY_OP
        i = cA - b; // INVALID_BINARY_OP
        i = cA - cA; // INVALID_BINARY_OP
        i = cA - cB; // INVALID_BINARY_OP

        i = cB - i; // INVALID_BINARY_OP
        i = cB - ia; // INVALID_BINARY_OP
        l = cB - l; // INVALID_BINARY_OP
        i = cB - la; // INVALID_BINARY_OP
        i = cB - b; // INVALID_BINARY_OP
        i = cB - cA; // INVALID_BINARY_OP
        i = cB - cB; // INVALID_BINARY_OP
    }
}
class A {}
class B {}
