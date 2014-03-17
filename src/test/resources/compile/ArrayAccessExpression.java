class ArrayAccessExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;

        i = ia[i];// OK!
        l = ia[i];  // OK!
        l = la[i];  // OK!
    }
}
