class GreaterEqualThanExpressionTest {
    public static void main(String[] args) {
        int i;
        long l;

        if (i >= i) {} // OK!
        if (i >= l) {} // OK!
        if (l >= i) {} // OK!
        if (l >= l) {} // OK!
    }
}
