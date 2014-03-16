class AssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;

        i = i; // OK!
        ia = ia;  // OK!
        l = i;  // OK!
        l = l;  // OK!
        la = la; // OK!
        cA = cA; // OK!
    }
}
class A {}
