class AssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;

        i = i; // OK!
        ia = ia;  // OK!
        b = b; // OK!
        cA = cA; // OK!
    }
}
class A {}
