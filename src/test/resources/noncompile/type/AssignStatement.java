class AssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        A = i;  // EXPECTED_VARIABLE_GOT_CLASS
        U = i;  // UNDECLARED_IDENTIFIER

        //i = i; // OK!
        i = ia; // INVALID_ASSIGNMENT
        i = b;  // INVALID_ASSIGNMENT
        i = cA; // INVALID_ASSIGNMENT

        ia = i;  // INVALID_ASSIGNMENT
        //ia = ia;  // OK!
        ia = b;  // INVALID_ASSIGNMENT
        ia = cA; // INVALID_ASSIGNMENT

        cA = i;  // INVALID_ASSIGNMENT
        cA = ia; // INVALID_ASSIGNMENT
        cA = b;  // INVALID_ASSIGNMENT
        //cA = cA; // OK!
        cA = cB; // INVALID_ASSIGNMENT
    }
}
class A {}
class B {}
