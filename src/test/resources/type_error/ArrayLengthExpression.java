class ArrayLengthExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        i = i.length;  // LENGTH_ON_NON_ARRAY_TYPE
        i = l.length;  // LENGTH_ON_NON_ARRAY_TYPE
        i = b.length;  // LENGTH_ON_NON_ARRAY_TYPE
        i = cA.length; // LENGTH_ON_NON_ARRAY_TYPE
    }
}
class A {}
class B {}
