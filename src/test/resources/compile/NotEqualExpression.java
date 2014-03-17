class NotEqualExpressionTest {
    public static void main(String[] args) {
        int i;
        int[] ia;
        long l;
        long[] la;
        boolean b;
        A cA;

        if (i != i) {}   // OK!
        if (i != l) {}   // OK!
        if (ia != ia) {}   // OK!
        if (l != i) {}   // OK!
        if (l != l) {}   // OK!
        if (la != la) {}   // OK!
        if (b != b) {}   // OK!
        if (cA != cA) {}   // OK!
    }
}
class A {}
