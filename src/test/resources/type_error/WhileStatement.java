class IfElseStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        while (i) {} // INVALID_CONDITION_TYPE
        while (ia) {} // INVALID_CONDITION_TYPE
        while (l) {} // INVALID_CONDITION_TYPE
        while (la) {} // INVALID_CONDITION_TYPE
        //while (b) {} // OK
        while (cA) {} // INVALID_CONDITION_TYPE
    }
}
class A {}
class B {}
