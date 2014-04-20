class EqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA1;
        A cA2;

        if (i == i) {}   // OK!
        if (ia == ia) {}   // OK!
        if (b == b) {}   // OK!
        if (cA1 == cA2) {}   // OK!
    }
}
class A {}
