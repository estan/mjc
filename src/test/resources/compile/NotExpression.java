class NotExpressionTest {
    public static void main(String[] args) {
        boolean b;
        if (!b) {} // OK!
        if (!!b) {} // OK!
    }
}
