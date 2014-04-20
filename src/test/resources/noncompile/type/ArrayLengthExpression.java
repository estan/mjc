class ArrayLengthExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;
        B cB;

        i = i.length;  // LENGTH_ON_NON_ARRAY
        //i = ia.length;  // OK!
        i = b.length;  // LENGTH_ON_NON_ARRAY
        i = cA.length; // LENGTH_ON_NON_ARRAY
    }
}
class A {}
class B {}
