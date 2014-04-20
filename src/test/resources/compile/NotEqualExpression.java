class NotEqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        boolean b;
        A cA;

        if (i != i) {}   // OK!
        if (ia != ia) {}   // OK!
        if (b != b) {}   // OK!
        if (cA != cA) {}   // OK!
    }
}
class A {}
