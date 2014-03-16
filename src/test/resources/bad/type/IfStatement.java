class IfStatementTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        if (i) {} // INVALID_CONDITION_TYPE
        if (ia) {} // INVALID_CONDITION_TYPE
        if (l) {} // INVALID_CONDITION_TYPE
        if (la) {} // INVALID_CONDITION_TYPE
        //if (b) {} // OK
        if (cA) {} // INVALID_CONDITION_TYPE
    }
}
class A {}
class B {}
