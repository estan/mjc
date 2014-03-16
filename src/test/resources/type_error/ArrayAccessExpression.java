class ArrayAccessExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        //i = ia[i];// OK!
        i = ia[ia]; // INVALID_INDEX_TYPE
        i = ia[l];  // INVALID_INDEX_TYPE
        i = ia[la]; // INVALID_INDEX_TYPE
        i = ia[b];  // INVALID_INDEX_TYPE
        i = ia[cA]; // INVALID_INDEX_TYPE

        //l = ia[i];  // OK!
        //l = la[i];  // OK!
        l = la[ia];  // INVALID_INDEX_TYPE
        l = la[l];   // INVALID_INDEX_TYPE
        l = la[la];  // INVALID_INDEX_TYPE
        l = la[b];   // INVALID_INDEX_TYPE
        l = la[cA];  // INVALID_INDEX_TYPE

        i = i[i];    // NOT_ARRAY_TYPE
        i = l[i];    // NOT_ARRAY_TYPE
        i = b[i];    // NOT_ARRAY_TYPE
        i = cA[i];   // NOT_ARRAY_TYPE
    }
}
class A {}
class B {}
