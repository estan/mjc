class ArrayLengthExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long[] la;

        i = ia.length;  // OK!
        i = la.length;  // OK!
    }
}
