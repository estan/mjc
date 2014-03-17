class LongExpressionTest {
    public static void main(String[] args) {
        long l;
        l = 0; // OK!
        l = 42; // OK!
        l = 9223372036854775807l; // OK! (MAX_LONG)
        l = 9223372036854775807L; // OK! (MAX_LONG)
    }
}
