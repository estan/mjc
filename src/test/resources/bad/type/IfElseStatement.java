class IfElseStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        if (i) {} else {} // INVALID_CONDITION_TYPE
        if (ia) {} else {} // INVALID_CONDITION_TYPE
        if (l) {} else {} // INVALID_CONDITION_TYPE
        if (la) {} else {} // INVALID_CONDITION_TYPE
        //if (b) {} else {} // OK
        if (cA) {} else {} // INVALID_CONDITION_TYPE
    }
}
class A {}
class B {}
