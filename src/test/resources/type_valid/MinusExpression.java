class MinusExpressionTest {
    public static void main(String[] args) {
        int i;
        long l;

        i = i - i; // OK!
        l = l - i; // OK!
        l = l - l; // OK!
    }
}
