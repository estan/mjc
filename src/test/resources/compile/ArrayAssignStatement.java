class ArrayAssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;

        ia[i] = i;  // OK!
        la[i] = l; // OK!
        la[i] = i; // OK!
    }
}
