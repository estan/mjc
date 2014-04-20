class ArrayAccessExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //i = ia[i];// OK!
        i = ia[ia]; // INVALID_INDEX_TYPE
        i = ia[b];  // INVALID_INDEX_TYPE
        i = ia[cA]; // INVALID_INDEX_TYPE

        i = i[i];    // NOT_ARRAY_TYPE
        i = b[i];    // NOT_ARRAY_TYPE
        i = cA[i];   // NOT_ARRAY_TYPE
    }
}
class A {}
class B {}
