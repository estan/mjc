class NewLongArrayExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;

        //ia = new int[i]; // OK!
        la = new long[ia]; // INVALID_SIZE_TYPE
        la = new long[l]; // INVALID_SIZE_TYPE
        la = new long[la]; // INVALID_SIZE_TYPE
        la = new long[b]; // INVALID_SIZE_TYPE
        la = new long[cA]; // INVALID_SIZE_TYPE
    }
}
class A {}
