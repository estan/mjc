class IfElseStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        if (i) {} else {} // INVALID_CONDITION_TYPE
        if (ia) {} else {} // INVALID_CONDITION_TYPE
        //if (b) {} else {} // OK
        if (cA) {} else {} // INVALID_CONDITION_TYPE
    }
}
class A {}
class B {}
