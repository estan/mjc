class LongExpressionTest {
    public static void main(String[] args) {
        long l;
        //l = 9223372036854775807l; // OK!
        //l = 9223372036854775807L; // OK!
        l = 9223372036854775808l; // INVALID_LONG_LITERAL
        l = 9223372036854775808L; // INVALID_LONG_LITERAL
    }
}
