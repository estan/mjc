class ArrayAssignStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        //ia[i] = i;  // OK!
        ia[ia] = i;   // INVALID_INDEX_TYPE
        ia[b] = i;    // INVALID_INDEX_TYPE
        ia[cA] = i;   // INVALID_INDEX_TYPE

        A[i] = i; // EXPECTED_VARIABLE_GOT_CLASS
        U[i] = i; // UNDECLARED_IDENTIFIER

        ia[i] = ia; // INVALID_ASSIGNMENT
        ia[i] = b;  // INVALID_ASSIGNMENT
        ia[i] = cA; // INVALID_ASSIGNMENT

        i[i] = i;  // NOT_ARRAY_TYPE
        b[i] = i;  // NOT_ARRAY_TYPE
        cA[i] = i; // NOT_ARRAY_TYPE
    }
}
class A {}
class B {}
