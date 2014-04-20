class WhileStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        while (i) {} // INVALID_CONDITION_TYPE
        while (ia) {} // INVALID_CONDITION_TYPE
        //while (b) {} // OK
        while (cA) {} // INVALID_CONDITION_TYPE
    }
}
class A {}
class B {}
