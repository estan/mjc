class ArrayLengthExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;
        B cB;

        i = i.length;  // LENGTH_ON_NON_ARRAY
        //i = ia.length;  // OK!
        i = l.length;  // LENGTH_ON_NON_ARRAY
        //i = la.length;  // OK!
        i = b.length;  // LENGTH_ON_NON_ARRAY
        i = cA.length; // LENGTH_ON_NON_ARRAY
    }
}
class A {}
class B {}
