class ArrayAccessExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        i = ia[ia]; // WRONG_INDEX_TYPE
        i = ia[l];  // WRONG_INDEX_TYPE
        i = ia[la]; // WRONG_INDEX_TYPE
        i = ia[b];  // WRONG_INDEX_TYPE
        i = ia[cA]; // WRONG_INDEX_TYPE

        l = la[ia];  // WRONG_INDEX_TYPE
        l = la[l];   // WRONG_INDEX_TYPE
        l = la[la];  // WRONG_INDEX_TYPE
        l = la[b];   // WRONG_INDEX_TYPE
        l = la[cA];  // WRONG_INDEX_TYPE

        i = i[i];    // NOT_ARRAY_TYPE
        i = l[i];    // NOT_ARRAY_TYPE
        i = b[i];    // NOT_ARRAY_TYPE
        i = cA[i];   // NOT_ARRAY_TYPE
    }
}
class A {}
class B {}
