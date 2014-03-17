class AssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        A = i;  // EXPECTED_VARIABLE_GOT_CLASS
        U = i;  // UNDECLARED_IDENTIFIER

        //i = i; // OK!
        i = ia; // INVALID_ASSIGNMENT
        i = l;  // INVALID_ASSIGNMENT
        i = la; // INVALID_ASSIGNMENT
        i = b;  // INVALID_ASSIGNMENT
        i = cA; // INVALID_ASSIGNMENT

        ia = i;  // INVALID_ASSIGNMENT
        //ia = ia;  // OK!
        ia = l;  // INVALID_ASSIGNMENT
        ia = la; // INVALID_ASSIGNMENT
        ia = b;  // INVALID_ASSIGNMENT
        ia = cA; // INVALID_ASSIGNMENT

        //l = i;  // OK!
        l = ia;  // INVALID_ASSIGNMENT
        //l = l;  // OK!
        l = la;  // INVALID_ASSIGNMENT
        l = b;   // INVALID_ASSIGNMENT
        l = cA;  // INVALID_ASSIGNMENT

        la = i;  // INVALID_ASSIGNMENT
        la = ia; // INVALID_ASSIGNMENT
        la = l;  // INVALID_ASSIGNMENT
        //la = la; // OK!
        la = b;  // INVALID_ASSIGNMENT
        la = cA; // INVALID_ASSIGNMENT

        cA = i;  // INVALID_ASSIGNMENT
        cA = ia; // INVALID_ASSIGNMENT
        cA = l;  // INVALID_ASSIGNMENT
        cA = la; // INVALID_ASSIGNMENT
        cA = b;  // INVALID_ASSIGNMENT
        //cA = cA; // OK!
        cA = cB; // INVALID_ASSIGNMENT
    }
}
class A {}
class B {}
